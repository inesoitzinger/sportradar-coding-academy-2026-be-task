package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class WinProbabilityService {

    private final MatchRepository matchRepository;

    public Double calcHome(Match m) {
        if (m.getStatus() != MatchStatus.scheduled) return null;
        var record = matchRepository.getRecord(m.getHomeTeam().getId());
        long wins = ((Number) record.get("wins")).longValue();
        long matches = ((Number) record.get("matches")).longValue();
        return matches == 0 ? null : (double) wins / matches;
    }
}


