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

    /**
     * Returns all leagues in the system.
     *
     * @return list of leagues
     */
    @GetMapping
    public List<LeagueDto> getAll() {
        return leagueService.findAll().stream()
                .map(mapper::toLeagueDto)
                .toList();
    }

    /**
     * Returns a single league by id.
     *
     * @param id league id
     * @return league details
     */
    @GetMapping("/{id}")
    public LeagueDto getOne(@PathVariable long id) {
        return mapper.toLeagueDto(leagueService.getById(id));
    }

    /**
     * Returns all teams that participate in the given league.
     *
     * @param id league id
     * @return list of teams for that league
     */
    @GetMapping("/{id}/teams")
    public List<TeamDto> getTeamsForLeague(@PathVariable long id) {
        return teamService.getTeamsForLeague(id).stream()
                .map(mapper::toTeamDto)
                .toList();
    }

}
