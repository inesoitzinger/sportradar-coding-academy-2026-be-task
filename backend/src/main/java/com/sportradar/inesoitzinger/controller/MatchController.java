package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final DtoMapper mapper;

    @GetMapping
    public List<MatchDto> getAll() {
        return matchService.findAll()
                .stream()
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MatchDto getOne(@PathVariable long id) {
        return mapper.toMatchDto(matchService.getById(id));
    }

    @GetMapping(params = "status")
    public List<MatchDto> getByStatus(@RequestParam String status) {
        MatchStatus enumStatus = MatchStatus.valueOf(status);
        return matchService.getByStatus(enumStatus).stream()
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping(params = "from")
    public List<MatchDto> getMatchesFromNow(@RequestParam String from) {
        if (!from.equalsIgnoreCase("now")) {
            throw new IllegalArgumentException("only supported value: now");
        }

        Instant now = Instant.now();

        return matchService.getFromNow(now)
                .stream()
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping(params = "leagueId")
    public List<MatchDto> getByLeague(@RequestParam long leagueId) {
        return matchService.getByLeagueId(leagueId)
                .stream()
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping(params = "teamId")
    public List<MatchDto> getByTeam(@RequestParam long teamId) {
        return matchService.getByTeamId(teamId)
                .stream()
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping(params = "sportId")
    public List<MatchDto> getBySport(@RequestParam long sportId) {
        return matchService.getBySportId(sportId)
                .stream()
                .map(mapper::toMatchDto)
                .toList();
    }


}
