package com.sportradar.inesoitzinger.dtos;

public record LeagueDto(
        Long id,
        String name,
        String country,
        String shortCode,
        SportDto sport
) {}
