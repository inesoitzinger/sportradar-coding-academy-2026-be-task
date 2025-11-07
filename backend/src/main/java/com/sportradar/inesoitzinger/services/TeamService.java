package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team getById(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team " + id + " not found"));
    }

    public List<Team> getBySportId(long sportId) {
        return teamRepository.findBySportId(sportId);
    }

    public List<Team> getByLeagueId(long leagueId) {
        return teamRepository.findByLeagueId(leagueId);
    }



}
