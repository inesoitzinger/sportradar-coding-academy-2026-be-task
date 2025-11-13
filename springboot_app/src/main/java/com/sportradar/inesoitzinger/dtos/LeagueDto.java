package com.sportradar.inesoitzinger.dtos;

/**
 * Response payload for a league.
 */
public record LeagueDto(

        /**
         * Unique league id.
         */
        Long id,

        /**
         * League name.
         */
        String name,

        /**
         * Country the league belongs to.
         */
        String country,

        /**
         * Short identifier of the league.
         */
        String shortCode,

        /**
         * Sport this league belongs to.
         */
        SportDto sport
) {}
