package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.TeamService;
import com.sportradar.inesoitzinger.services.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final LeagueService leagueService;
    private final DtoMapper mapper;

    @GetMapping
    public List<TeamDto> getAll() {
        return teamService.findAll().stream()
                .map(mapper::toTeamDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TeamDto getOne(@PathVariable long id) {
        return mapper.toTeamDto(teamService.getById(id));
    }

    @GetMapping("/{id}/leagues")
    public List<LeagueDto> getLeaguesForTeam(@PathVariable long id) {
        teamService.getById(id); // just to throw 404 if unknown team
        return leagueService.getByTeamId(id).stream()
                .map(mapper::toLeagueDto)
                .toList();
    }
}

