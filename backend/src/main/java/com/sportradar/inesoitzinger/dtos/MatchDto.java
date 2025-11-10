package com.sportradar.inesoitzinger.dtos;

import java.time.Instant;

public record MatchDto(
        Long id,
        String title,
        TeamDto homeTeam,
        TeamDto awayTeam,
        VenueDto venue,
        LeagueDto league,
        Instant startAt,
        Integer homeScore,
        Integer awayScore,
        String status,
        Double homeWinProbability

) {}
