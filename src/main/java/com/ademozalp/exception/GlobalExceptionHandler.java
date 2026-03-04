package com.ademozalp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityException.class)
    public String handleCityException(CityException ex) {
        log.error("CityException occurred: {}", ex.getMessage());
        return "An unexpected error occurred. Please try again later.";
    }
}
