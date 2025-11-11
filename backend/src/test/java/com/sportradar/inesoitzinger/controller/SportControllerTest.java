package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.LeagueDto;
import com.sportradar.inesoitzinger.dtos.SportDto;
import com.sportradar.inesoitzinger.dtos.TeamDto;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.League;
import com.sportradar.inesoitzinger.models.Sport;
import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.services.LeagueService;
import com.sportradar.inesoitzinger.services.SportService;
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

@WebMvcTest(SportController.class)
class SportControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean SportService sportService;
    @MockBean LeagueService leagueService;
    @MockBean TeamService teamService;
    @MockBean DtoMapper mapper;

    @Test
    void getAll_returnsList() throws Exception {
        Sport s = new Sport();
        s.setId(1L);
        s.setName("Football");

        Mockito.when(sportService.findAll()).thenReturn(List.of(s));
        Mockito.when(mapper.toSportDto(s)).thenReturn(new SportDto(1L, "Football"));

        mvc.perform(get("/sports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Football"));
    }

    @Test
    void getOne_returnsDto() throws Exception {
        Sport s = new Sport();
        s.setId(2L);
        s.setName("Basketball");

        Mockito.when(sportService.getById(2L)).thenReturn(s);
        Mockito.when(mapper.toSportDto(s)).thenReturn(new SportDto(2L, "Basketball"));

        mvc.perform(get("/sports/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Basketball"));
    }

    @Test
    void getOne_notFound_returns400() throws Exception {
        Mockito.when(sportService.getById(anyLong()))
                .thenThrow(new DomainRuleViolation("sport not found"));

        mvc.perform(get("/sports/999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLeaguesForSport_returnsList() throws Exception {
        League l = new League();
        l.setId(10L);
        l.setName("Premier League");

        Mockito.when(leagueService.getBySportId(1L)).thenReturn(List.of(l));
        Mockito.when(mapper.toLeagueDto(l))
                .thenReturn(new LeagueDto(10L, "Premier League", null, null, null));

        mvc.perform(get("/sports/1/leagues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Premier League"));
    }

    @Test
    void getTeamsForSport_returnsList() throws Exception {
        Team t = new Team();
        t.setId(123L);
        t.setName("Bayern");

        Mockito.when(teamService.getBySportId(1L)).thenReturn(List.of(t));
        Mockito.when(mapper.toTeamDto(t))
                .thenReturn(new TeamDto(123L, "Bayern", null));

        mvc.perform(get("/sports/1/teams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(123))
                .andExpect(jsonPath("$[0].name").value("Bayern"));
    }

    @Test
    void getLeaguesForSport_sportNotFound_returns400() throws Exception {
        Mockito.when(leagueService.getBySportId(anyLong()))
                .thenThrow(new DomainRuleViolation("sport not found"));

        mvc.perform(get("/sports/987/leagues"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTeamsForSport_sportNotFound_returns400() throws Exception {
        Mockito.when(teamService.getBySportId(anyLong()))
                .thenThrow(new DomainRuleViolation("sport not found"));

        mvc.perform(get("/sports/987/teams"))
                .andExpect(status().isBadRequest());
    }
}
