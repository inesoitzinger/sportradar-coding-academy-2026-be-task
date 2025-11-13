package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.exceptions.NotFoundException;
import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final LeagueRepository leagueRepository;
    private final SportRepository sportRepository;

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team getById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("match not found"));
    }

    public List<Team> getBySportId(long sportId) {
        sportRepository.findById(sportId)
                .orElseThrow(() -> new NotFoundException("sport not found"));

        return teamRepository.findBySportId(sportId);
    }


    public List<Team> getByLeagueId(long leagueId) {
        return teamRepository.findByLeagueId(leagueId);
    }

    public List<Team> getTeamsForLeague(long leagueId){
        leagueRepository.findById(leagueId)
                .orElseThrow(() -> new NotFoundException("league not found"));

        return teamRepository.findByLeagueId(leagueId);
    }




}
