package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final DtoMapper dtoMapper;

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match getById(long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Match " + id + " not found"));
    }

    public List<Match> getByStatus(MatchStatus status) {
        return matchRepository.findByStatus(status);
    }

    public List<Match> getFromNow(Instant now) {
        return matchRepository.findByStartAtAfter(now);
    }

    public List<Match> getByLeagueId(long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
    }

    public List<Match> getByTeamId(long teamId) {
        return matchRepository.findByTeamId(teamId);
    }

    public List<Match> getBySportId(long sportId) {
        return matchRepository.findBySportId(sportId);
    }

    public List<Match> getByVenueId(long venueId) {
        return matchRepository.findByVenueId(venueId);
    }

}
