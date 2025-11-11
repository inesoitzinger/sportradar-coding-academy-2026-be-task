package com.sportradar.inesoitzinger.dtos;

/**
 * Response payload for a venue (stadium).
 */
public record VenueDto(

        /**
         * Unique venue id.
         */
        Long id,

        /**
         * Name of the venue.
         */
        String name,

        /**
         * City where the venue is located.
         */
        String city,

        /**
         * Capacity of venue.
         */
        Long capacity
) {}
