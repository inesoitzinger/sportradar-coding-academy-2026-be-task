package com.sportradar.inesoitzinger.mappers;

import com.sportradar.inesoitzinger.dtos.*;
import com.sportradar.inesoitzinger.models.*;

import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public SportDto toSportDto(Sport e) {
        return new SportDto(e.getId(), e.getName());
    }

    public LeagueDto toLeagueDto(League e) {
        return new LeagueDto(
                e.getId(),
                e.getName(),
                e.getCountry(),
                e.getShortCode(),
                toSportDto(e.getSport())
        );
    }

    public TeamDto toTeamDto(Team e) {
        return new TeamDto(
                e.getId(),
                e.getName()
        );
    }

    public VenueDto toVenueDto(Venue e) {
        return new VenueDto(
                e.getId(),
                e.getName(),
                e.getCity(),
                Math.toIntExact(e.getCapacity())
        );
    }

    public MatchDto toMatchDto(Match e) {
        return new MatchDto(
                e.getId(),
                e.getTitle(),
                toTeamDto(e.getHomeTeam()),
                toTeamDto(e.getAwayTeam()),
                toVenueDto(e.getVenue()),
                toLeagueDto(e.getLeague()),
                e.getStartAt(),         // Instant
                e.getHomeScore(),
                e.getAwayScore(),
                e.getStatus().name()    // Enum → String
        );
    }
}
