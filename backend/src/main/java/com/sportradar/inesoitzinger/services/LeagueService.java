package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.models.League;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public List<League> findAll() {
        return leagueRepository.findAll();
    }

    public League getById(long id) {
        return leagueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("League " + id + " not found"));
    }

    public List<League> getBySportId(long sportId) {
        return leagueRepository.findBySportId(sportId);
    }

    public List<League> getByTeamId(long teamId) {
        return leagueRepository.findByTeamId(teamId);
    }


}
