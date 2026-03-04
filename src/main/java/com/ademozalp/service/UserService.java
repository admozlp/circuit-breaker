package com.ademozalp.service;

import com.ademozalp.dto.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    Map<String, List<CityResponse>> users = new ConcurrentHashMap<>();

    private final CityService cityService;

    public void createUser(String name, String city) {
        List<CityResponse> cityResponse = cityService.getAllCities();

        users.put(name, cityResponse);

        printUsers();
    }

    private void printUsers() {
        log.info("Current users:");
        users.forEach((name, cities) -> log.info("User: {}, Cities: {}", name, cities));
    }
}