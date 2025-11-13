package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.dtos.MatchSearchDto;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.exceptions.NotFoundException;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
import com.sportradar.inesoitzinger.services.WinProbabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final LeagueRepository leagueRepository;

    private final DtoMapper dtoMapper;

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match getById(long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("match not found"));
    }

    public List<Match> search(MatchSearchDto dto) {

        if (dto.q() != null && !dto.q().isBlank()) {
            return matchRepository.searchByText(dto.q());
        }

        return matchRepository.searchDynamic(
                dto.sportId(),
                dto.status() == null || dto.status().isBlank() ? null : dto.status(),
                dto.venueId(),
                dto.leagueId(),
                dto.teamId()
        );
    }

    @Transactional
    public Match createMatch(CreateMatchRequestDto req) {

        var home   = teamRepository.getReferenceById(req.homeTeamId());
        var away   = teamRepository.getReferenceById(req.awayTeamId());
        var league = leagueRepository.getReferenceById(req.leagueId());
        var venue  = venueRepository.getReferenceById(req.venueId());

        Match m = Match.schedule(req.title(), req.startAt(), home, away, league, venue);

        return matchRepository.save(m);
    }


}
