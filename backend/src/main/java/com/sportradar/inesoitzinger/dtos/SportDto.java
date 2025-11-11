package com.sportradar.inesoitzinger.dtos;

/**
 * Response payload for a sport.
 */
public record SportDto(

        /**
         * Unique sport id.
         */
        Long id,

        /**
         * Name of the sport (for example: "Football", "Tennis", "Basketball").
         */
        String name
) {}
