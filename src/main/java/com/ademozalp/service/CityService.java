package com.ademozalp.service;

import com.ademozalp.client.CityClient;
import com.ademozalp.dto.ApiResponse;
import com.ademozalp.dto.CityResponse;
import com.ademozalp.exception.CityException;
import com.ademozalp.exception.RetryableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityClient cityClient;
    private final RetryRegistry retryRegistry;

    @PostConstruct
    public void registerRetryEvents() {
        retryRegistry.retry("cityService").getEventPublisher()
                .onRetry(e -> log.warn("[RETRY] Deneme #{} - Hata: {}",
                        e.getNumberOfRetryAttempts(), e.getLastThrowable().getMessage()))
                .onSuccess(e -> log.info("[RETRY] Başarılı - {} denemeden sonra",
                        e.getNumberOfRetryAttempts()))
                .onError(e -> log.error("[RETRY] Tüm denemeler bitti ({} deneme) - Hata: {}",
                        e.getNumberOfRetryAttempts(), e.getLastThrowable().getMessage()))
                .onIgnoredError(e -> log.warn("[RETRY] Ignore edilen hata: {}",
                        e.getLastThrowable().getMessage()));
    }

    @Retry(name = "cityService", fallbackMethod = "getAllCitiesFallback")
    @CircuitBreaker(name = "cityService")
    public List<CityResponse> getAllCities() {
        ApiResponse<List<CityResponse>> response = cityClient.getAllCities();
        validateResponse(response);
        return response.getData();
    }

    private void validateResponse(ApiResponse<?> response) {
        HttpStatus status = response.getStatus();

        if (status.is5xxServerError() || status == HttpStatus.TOO_MANY_REQUESTS) {
            throw new RetryableException("Retryable error from city service: " + status);
        }

        if (status.is4xxClientError()) {
            throw new CityException("Client error from city service: " + status);
        }
    }

    public List<CityResponse> getAllCitiesFallback(Throwable ex) {
        int maxAttempts = retryRegistry.retry("cityService").getRetryConfig().getMaxAttempts();
        log.error("City service unavailable after {} attempts: {}", maxAttempts, ex.getMessage());
        throw new CityException("City service is currently unavailable. Please try again later.");
    }
}