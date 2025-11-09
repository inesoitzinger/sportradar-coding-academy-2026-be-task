package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    public List<Match> search(Map<String, String> params) {
        if (params.containsKey("q") && !params.get("q").isBlank()) {
            return matchRepository.searchByText(params.get("q"));
        }

        Long sportId  = params.containsKey("sportId")  && !params.get("sportId").isBlank()
                ? Long.parseLong(params.get("sportId")) : null;

        String status = params.containsKey("status") && !params.get("status").isBlank()
                ? params.get("status") : null;

        Long venueId  = params.containsKey("venueId")  && !params.get("venueId").isBlank()
                ? Long.parseLong(params.get("venueId")) : null;

        Long leagueId = params.containsKey("leagueId") && !params.get("leagueId").isBlank()
                ? Long.parseLong(params.get("leagueId")) : null;

        Long teamId   = params.containsKey("teamId")   && !params.get("teamId").isBlank()
                ? Long.parseLong(params.get("teamId")) : null;

        return matchRepository.searchDynamic(sportId, status, venueId, leagueId, teamId);
    }


}
