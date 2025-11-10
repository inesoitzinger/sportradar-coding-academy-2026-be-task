package com.sportradar.inesoitzinger.dtos;

public record MatchSearchDto(
        String q,
        Long sportId,
        String status,
        Long venueId,
        Long leagueId,
        Long teamId
) {}

