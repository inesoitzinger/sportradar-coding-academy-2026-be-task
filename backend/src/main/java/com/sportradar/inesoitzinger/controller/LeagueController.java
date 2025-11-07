package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.LeagueService;
import com.sportradar.inesoitzinger.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leagues")
@RequiredArgsConstructor
public class LeagueController {

    private final LeagueService leagueService;
    private final TeamService teamService;
    private final DtoMapper mapper;

    @GetMapping
    public List<LeagueDto> getAll() {
        return leagueService.findAll().stream()
                .map(mapper::toLeagueDto)
                .toList();
    }

    @GetMapping("/{id}")
    public LeagueDto getOne(@PathVariable long id) {
        return mapper.toLeagueDto(leagueService.getById(id));
    }

    @GetMapping("/{id}/teams")
    public List<TeamDto> getTeamsForLeague(@PathVariable long id) {
        leagueService.getById(id); // nur für 404 wenn nicht existent

        return teamService.getByLeagueId(id).stream()
                .map(mapper::toTeamDto)
                .toList();
    }

}

