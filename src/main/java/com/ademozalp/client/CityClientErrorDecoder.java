package com.ademozalp.client;

import com.ademozalp.exception.RetryableException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CityClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.warn("Feign error - method: {}, status: {}", methodKey, response.status());

        return switch (response.status()) {
            case 500, 502, 503, 504 -> new RetryableException(
                    "Server error, will retry. Status: " + response.status()
            );
            case 429 -> new RetryableException(
                    "Rate limited, will retry. Status: " + response.status()
            );
            default -> defaultDecoder.decode(methodKey, response);
        };
    }
}