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

    /**
     * Returns all sports.
     *
     * @return list of sports
     */
    @GetMapping
    public List<SportDto> getAll() {
        return sportService.findAll()
                .stream()
                .map(mapper::toSportDto)
                .toList();
    }

    /**
     * Returns one sport by id.
     *
     * @param id sport id
     * @return sport details
     */
    @GetMapping("/{id}")
    public SportDto getOne(@PathVariable long id) {
        return mapper.toSportDto(sportService.getById(id));
    }

    /**
     * Returns all leagues that belong to the given sport.
     *
     * @param id sport id
     * @return list of leagues for that sport
     */
    @GetMapping("/{id}/leagues")
    public List<LeagueDto> getLeaguesForSport(@PathVariable long id) {
        return leagueService.getBySportId(id).stream()
                .map(mapper::toLeagueDto)
                .toList();
    }

    /**
     * Returns all teams that belong to the given sport.
     *
     * @param id sport id
     * @return list of teams for that sport
     */
    @GetMapping("/{id}/teams")
    public List<TeamDto> getTeamsForSport(@PathVariable long id) {
        return teamService.getBySportId(id).stream()
                .map(mapper::toTeamDto)
                .toList();
    }
}
