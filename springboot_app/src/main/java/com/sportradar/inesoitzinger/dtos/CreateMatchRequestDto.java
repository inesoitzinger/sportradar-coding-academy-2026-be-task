package com.sportradar.inesoitzinger.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Request payload used to create a new match.
 * All domain rules are validated inside the Match aggregate.
 */
public record CreateMatchRequestDto(

        /**
         * Display name of the match (for example "Bayern vs Dortmund").
         */
        @NotBlank String title,

        /**
         * Kickoff timestamp. Must be in the future.
         */
        @NotNull Instant startAt,

        /**
         * League in which this match happens.
         */
        @NotNull Long leagueId,

        /**
         * Venue (stadium) where the match is played.
         */
        @NotNull Long venueId,

        /**
         * Home team id.
         */
        @NotNull Long homeTeamId,

        /**
         * Away team id.
         */
        @NotNull Long awayTeamId
) {}
