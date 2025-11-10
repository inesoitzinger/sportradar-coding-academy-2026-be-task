package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.League;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final SportRepository sportRepository;
    private final TeamRepository teamRepository;

    public List<League> findAll() {
        return leagueRepository.findAll();
    }

    public League getById(long id) {
        return leagueRepository.findById(id)
                .orElseThrow(() -> new DomainRuleViolation("league not found"));
    }

    public List<League> getBySportId(long sportId) {
        sportRepository.findById(sportId)
                .orElseThrow(() -> new DomainRuleViolation("sport not found"));

        return leagueRepository.findBySportId(sportId);
    }


    public List<League> getByTeamId(long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new DomainRuleViolation("team not found"));

        return leagueRepository.findByTeamId(teamId);
    }



}
