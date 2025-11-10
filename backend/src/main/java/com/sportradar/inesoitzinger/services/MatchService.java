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

    private static Long toLongOrNull(String v) {
        return (v == null || v.isBlank()) ? null : Long.valueOf(v);
    }

    public List<Match> search(Map<String, String> params) {

        if (params.containsKey("q") && !params.get("q").isBlank()) {
            return matchRepository.searchByText(params.get("q"));
        }

        Long sportId  = toLongOrNull(params.get("sportId"));
        String status = params.getOrDefault("status", "").isBlank() ? null : params.get("status");
        Long venueId  = toLongOrNull(params.get("venueId"));
        Long leagueId = toLongOrNull(params.get("leagueId"));
        Long teamId   = toLongOrNull(params.get("teamId"));

        return matchRepository.searchDynamic(sportId, status, venueId, leagueId, teamId);
    }


    public Double calcHomeWinProbability(Match m) {
        System.out.println("STATUS = " + m.getStatus());

        if (!m.getStatus().equals(MatchStatus.scheduled)) {
            return null;
        }


        Map<String,Object> record = matchRepository.getRecord(m.getHomeTeam().getId());
        long wins = ((Number)record.get("wins")).longValue();
        long matches = ((Number)record.get("matches")).longValue();

        if (matches == 0) return null;
        return (double) wins / matches;
    }




}
