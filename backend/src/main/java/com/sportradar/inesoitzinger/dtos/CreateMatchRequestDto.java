package com.sportradar.inesoitzinger.dtos;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Instant;

public record CreateMatchRequestDto(
        @NotBlank String title,
        @NotNull Instant startAt,
        @NotNull Long leagueId,
        @NotNull Long venueId,
        @NotNull Long homeTeamId,
        @NotNull Long awayTeamId
){}
