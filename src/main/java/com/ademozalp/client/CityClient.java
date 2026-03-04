package com.ademozalp.client;

import com.ademozalp.dto.ApiResponse;
import com.ademozalp.dto.CityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "city-client", url = "localhost:4040/cities")
public interface CityClient {

    @GetMapping
    ApiResponse<List<CityResponse>> getAllCities();
}
