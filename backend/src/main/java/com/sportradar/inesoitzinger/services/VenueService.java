package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.Venue;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final LeagueRepository leagueRepository;

    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    public Venue getById(long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new DomainRuleViolation("match not found"));
    }

}
