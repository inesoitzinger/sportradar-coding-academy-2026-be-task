package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.exceptions.NotFoundException;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.League;
import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.services.LeagueService;
import com.sportradar.inesoitzinger.services.TeamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeamController.class)
class TeamControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TeamService teamService;

    @MockBean
    LeagueService leagueService;

    @MockBean
    DtoMapper mapper;

    @Test
    void getAll_returnsList() throws Exception {

        Team t = new Team();
        t.setId(10L);
        t.setName("Liverpool");

        Mockito.when(teamService.findAll()).thenReturn(List.of(t));

        Mockito.when(mapper.toTeamDto(t))
                .thenReturn(new TeamDto(10L, "Liverpool", null));

        mvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Liverpool"));
    }

    @Test
    void getOne_returnsDto() throws Exception {

        Team t = new Team();
        t.setId(20L);
        t.setName("Arsenal");

        Mockito.when(teamService.getById(20L)).thenReturn(t);

        Mockito.when(mapper.toTeamDto(t))
                .thenReturn(new TeamDto(20L, "Arsenal", null));

        mvc.perform(get("/teams/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(20))
                .andExpect(jsonPath("$.name").value("Arsenal"));
    }

    @Test
    void getLeaguesForTeam_returnsList() throws Exception {

        League l = new League();
        l.setId(30L);
        l.setName("Premier League");

        Mockito.when(leagueService.getByTeamId(99L)).thenReturn(List.of(l));

        Mockito.when(mapper.toLeagueDto(l))
                .thenReturn(new LeagueDto(30L, "Premier League", null, null, null));

        mvc.perform(get("/teams/99/leagues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].name").value("Premier League"));
    }

    @Test
    void getLeaguesForTeam_teamNotFound_returns404() throws Exception {

        Mockito.when(leagueService.getByTeamId(anyLong()))
                .thenThrow(new NotFoundException("team not found"));

        mvc.perform(get("/teams/999/leagues"))
                .andExpect(status().isNotFound());
    }
}
