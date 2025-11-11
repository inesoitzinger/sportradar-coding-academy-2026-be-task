package com.sportradar.inesoitzinger.dtos;

import java.time.Instant;

/**
 * Response payload for a match.
 */
public record MatchDto(

        /**
         * Unique match id.
         */
        Long id,

        /**
         * Display name of the match.
         */
        String title,

        /**
         * The home team.
         */
        TeamDto homeTeam,

        /**
         * The away team.
         */
        TeamDto awayTeam,

        /**
         * The venue where the match is played.
         */
        VenueDto venue,

        /**
         * The league this match belongs to.
         */
        LeagueDto league,

        /**
         * Scheduled kickoff timestamp.
         */
        Instant startAt,

        /**
         * Current home score, null if not started.
         */
        Integer homeScore,

        /**
         * Current away score, null if not started.
         */
        Integer awayScore,

        /**
         * Current match status (string value of MatchStatus enum).
         */
        String status,

        /**
         * Approximate probability of home team winning (only present for scheduled matches).
         */
        Double homeWinProbability
) {}
