package com.sportradar.inesoitzinger.dtos;

public record VenueDto(
        Long id,
        String name,
        String city,
        Integer capacity
) {}
