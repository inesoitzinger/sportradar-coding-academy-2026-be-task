package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
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

@WebMvcTest(LeagueController.class)
class LeagueControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    LeagueService leagueService;

    @MockBean
    TeamService teamService;

    @MockBean
    DtoMapper mapper;


    @Test
    void getAll_returnsList() throws Exception {

        League l = new League();
        l.setId(22L);
        l.setName("Bundesliga");

        Mockito.when(leagueService.findAll()).thenReturn(List.of(l));

        Mockito.when(mapper.toLeagueDto(l)).thenReturn(
                new LeagueDto(22L, "Bundesliga", null, null, null)
        );

        mvc.perform(get("/leagues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(22))
                .andExpect(jsonPath("$[0].name").value("Bundesliga"));
    }

    @Test
    void getOne_returnsDto() throws Exception {

        League l = new League();
        l.setId(55L);
        l.setName("Premier League");

        Mockito.when(leagueService.getById(55L)).thenReturn(l);

        Mockito.when(mapper.toLeagueDto(l)).thenReturn(
                new LeagueDto(55L, "Premier League", null, null, null)
        );

        mvc.perform(get("/leagues/55"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(55))
                .andExpect(jsonPath("$.name").value("Premier League"));
    }

    @Test
    void getTeamsForLeague_returnsList() throws Exception {

        Team t = new Team();
        t.setId(100L);
        t.setName("Bayern");

        Mockito.when(teamService.getTeamsForLeague(55L)).thenReturn(List.of(t));

        TeamDto dto = new TeamDto(
                100L,
                "Bayern",
                null // sport oder venue dto → irrelevant für diesen test
        );

        Mockito.when(mapper.toTeamDto(t)).thenReturn(dto);

        mvc.perform(get("/leagues/55/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].name").value("Bayern"));
    }


    @Test
    void getTeamsForLeague_leagueNotFound_returns400() throws Exception {

        Mockito.when(teamService.getTeamsForLeague(anyLong()))
                .thenThrow(new DomainRuleViolation("league not found"));

        mvc.perform(get("/leagues/999/teams"))
                .andExpect(status().isBadRequest());
    }
}
