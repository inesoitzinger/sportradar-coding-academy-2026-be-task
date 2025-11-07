package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.SportDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.SportService;
import com.sportradar.inesoitzinger.services.LeagueService;
import com.sportradar.inesoitzinger.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sports")
@RequiredArgsConstructor
public class SportController {

    private final SportService sportService;
    private final LeagueService leagueService;
    private final TeamService teamService;
    private final DtoMapper mapper;

    @GetMapping
    public List<SportDto> getAll() {
        return sportService.findAll()
                .stream()
                .map(mapper::toSportDto)
                .toList();
    }

    @GetMapping("/{id}")
    public SportDto getOne(@PathVariable long id) {
        return mapper.toSportDto(sportService.getById(id));
    }

    @GetMapping("/{id}/leagues")
    public List<LeagueDto> getLeaguesForSport(@PathVariable long id) {
        sportService.getById(id);
        return leagueService.getBySportId(id).stream()
                .map(mapper::toLeagueDto)
                .toList();
    }

    @GetMapping("/{id}/teams")
    public List<TeamDto> getTeamsForSport(@PathVariable long id) {
        sportService.getById(id); // damit 404 wenn sport id falsch
        return teamService.getBySportId(id).stream()
                .map(mapper::toTeamDto)
                .toList();
    }



}
