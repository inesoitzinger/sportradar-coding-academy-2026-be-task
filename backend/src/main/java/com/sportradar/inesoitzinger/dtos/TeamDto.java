package com.sportradar.inesoitzinger.dtos;

/**
 * Response payload for a team.
 */
public record TeamDto(

        /**
         * Unique team id.
         */
        Long id,

        /**
         * Team name.
         */
        String name,

        /**
         * Sport this team belongs to.
         */
        SportDto sport
) {}
