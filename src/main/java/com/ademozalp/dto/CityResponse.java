package com.ademozalp.dto;

public record CityResponse(
        Integer id,
        String name
) {

    @Override
    public String toString() {
        return "{id=" + id + ", name='" + name + "'}";
    }
}
