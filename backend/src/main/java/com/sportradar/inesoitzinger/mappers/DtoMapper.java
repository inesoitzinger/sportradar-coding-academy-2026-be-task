package com.sportradar.inesoitzinger.mappers;

import com.sportradar.inesoitzinger.dtos.*;
import com.sportradar.inesoitzinger.enums.MatchStatus;
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
                e.getName(),
                toSportDto(e.getSport())
        );
    }

    public VenueDto toVenueDto(Venue e) {
        return new VenueDto(
                e.getId(),
                e.getName(),
                e.getCity(),
                (long) Math.toIntExact(e.getCapacity())
        );
    }

    public MatchDto toMatchDto(Match m, Double p){

        return new MatchDto(
                m.getId(),
                m.getTitle(),
                toTeamDto(m.getHomeTeam()),
                toTeamDto(m.getAwayTeam()),
                toVenueDto(m.getVenue()),
                toLeagueDto(m.getLeague()),
                m.getStartAt(),
                m.getHomeScore(),
                m.getAwayScore(),
                m.getStatus().toString(),
                p
        );
    }

    public Match fromCreateDto(CreateMatchRequestDto dto) {
        Match m = new Match();
        m.setTitle(dto.title());
        m.setStartAt(dto.startAt());
        m.setStatus(MatchStatus.scheduled);
        return m;
    }



}
