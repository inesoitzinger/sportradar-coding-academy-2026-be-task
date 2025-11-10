package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
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
                .orElseThrow(() -> new DomainRuleViolation("match not found"));
    }

    public MatchDto getDtoById(long id) {
        Match m = getById(id);
        Double p = calcHomeWinProbability(m);
        return dtoMapper.toMatchDto(m, p);
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

        if (!m.getStatus().equals(MatchStatus.scheduled)) {
            return null;
        }


        Map<String,Object> record = matchRepository.getRecord(m.getHomeTeam().getId());
        long wins = ((Number)record.get("wins")).longValue();
        long matches = ((Number)record.get("matches")).longValue();

        if (matches == 0) return null;
        return (double) wins / matches;
    }


    @Transactional
    public Match createMatch(CreateMatchRequestDto req) {

        if (req.homeTeamId().equals(req.awayTeamId())) {
            throw new DomainRuleViolation("homeTeam and awayTeam cannot be the same");
        }

        var home = teamRepository.findById(req.homeTeamId())
                .orElseThrow(() -> new DomainRuleViolation("homeTeam not found"));

        var away = teamRepository.findById(req.awayTeamId())
                .orElseThrow(() -> new DomainRuleViolation("awayTeam not found"));

        var league = leagueRepository.findById(req.leagueId())
                .orElseThrow(() -> new DomainRuleViolation("league not found"));

        var venue = venueRepository.findById(req.venueId())
                .orElseThrow(() -> new DomainRuleViolation("venue not found"));

        if (!home.getSport().getId().equals(league.getSport().getId())
                || !away.getSport().getId().equals(league.getSport().getId())) {
            throw new DomainRuleViolation("both teams must match the league's sport");
        }

        if (req.startAt().isBefore(Instant.now())) {
            throw new DomainRuleViolation("startAt must be in the future");
        }

        Match m = new Match();
        m.setTitle(req.title());
        m.setStartAt(req.startAt());
        m.setStatus(MatchStatus.scheduled);
        m.setHomeTeam(home);
        m.setAwayTeam(away);
        m.setLeague(league);
        m.setVenue(venue);

        return matchRepository.save(m);
    }







}
