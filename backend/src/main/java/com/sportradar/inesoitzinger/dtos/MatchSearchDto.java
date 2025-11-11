package com.sportradar.inesoitzinger.dtos;

/**
 * Optional parameters used to filter matches.
 * If q is provided, a full text search is performed.
 */
public record MatchSearchDto(

        /**
         * Free text search over title.
         * This overrides all other filters if present.
         */
        String q,

        /**
         * Only return matches for this sport id.
         */
        Long sportId,

        /**
         * Only return matches with this status (string value of MatchStatus).
         */
        String status,

        /**
         * Only return matches played in this venue.
         */
        Long venueId,

        /**
         * Only return matches in this league.
         */
        Long leagueId,

        /**
         * Only return matches where this team participates.
         */
        Long teamId
) {}
