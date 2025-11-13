package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.MatchService;
import com.sportradar.inesoitzinger.services.WinProbabilityService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MatchService matchService;

    @MockBean
    WinProbabilityService winProbabilityService;

    @MockBean
    DtoMapper mapper;

    @Test
    void getMatchById_returnsDto() throws Exception {

        // domain match dummy
        Match m = new Match();
        m.setId(5L);
        m.setTitle("test");
        m.setStatus(MatchStatus.scheduled);
        m.setStartAt(Instant.now());

        // service returns domain match
        Mockito.when(matchService.getById(5L))
                .thenReturn(m);

        // probability
        Mockito.when(winProbabilityService.calcHome(m)).thenReturn(0.42);

        // dto mapping (constructor args exactly as in actual DTO!)
        MatchDto dto = new MatchDto(
                5L,
                "test",
                null, // homeTeam
                null, // awayTeam
                null, // venue
                null, // league
                m.getStartAt(),
                null,
                null,
                "scheduled",
                0.42
        );

        Mockito.when(mapper.toMatchDto(m, 0.42)).thenReturn(dto);

        mvc.perform(get("/matches/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("test"))
                .andExpect(jsonPath("$.homeWinProbability").value(0.42));
    }

    @Test
    void search_withQueryParam_callsService_andReturns200() throws Exception {

        Mockito.when(matchService.search(Mockito.any()))
                .thenReturn(List.of()); // just dummy

        mvc.perform(get("/matches?q=test"))
                .andExpect(status().isOk());

        // verify that search is called with a dto that contains q="test"
        Mockito.verify(matchService)
                .search(Mockito.argThat(dto -> "test".equals(dto.q())));
    }

    @Test
    void search_withoutQ_returnsList() throws Exception {

        // domain matches
        Match m = new Match();
        m.setId(5L);
        m.setTitle("MatchTitle");
        m.setStartAt(Instant.now());

        Mockito.when(matchService.search(Mockito.any()))
                .thenReturn(List.of(m));

        // map to DTO
        MatchDto dto = new MatchDto(
                5L,
                "MatchTitle",
                null,null,null,null,
                m.getStartAt(),
                null,null,
                "scheduled",
                null
        );

        Mockito.when(winProbabilityService.calcHome(m)).thenReturn(null);

        Mockito.when(mapper.toMatchDto(m, null)).thenReturn(dto);

        mvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].title").value("MatchTitle"));
    }

    @Test
    void createMatch_returnsId() throws Exception {

        // mock returned match (domain)
        Match saved = new Match();
        saved.setId(55L);

        Mockito.when(matchService.createMatch(Mockito.any())).thenReturn(saved);

        String json = """
            {
              "title": "TestMatch",
              "startAt": "%s",
              "leagueId": 20,
              "venueId": 30,
              "homeTeamId": 10,
              "awayTeamId": 11
            }
            """.formatted(Instant.now().plusSeconds(3600).toString());

        mvc.perform(
                        post("/matches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("55"));

        // verify correct DTO arrived at service
        ArgumentCaptor<CreateMatchRequestDto> captor = ArgumentCaptor.forClass(CreateMatchRequestDto.class);
        Mockito.verify(matchService).createMatch(captor.capture());

        CreateMatchRequestDto dto = captor.getValue();
        assertThat(dto.title()).isEqualTo("TestMatch");
        assertThat(dto.leagueId()).isEqualTo(20L);
        assertThat(dto.homeTeamId()).isEqualTo(10L);
    }

    @Test
    void createMatch_homeEqualsAway_returns400() throws Exception {

        // wenn Service aufgerufen wird, Domain wirft Ausnahme
        Mockito.when(matchService.createMatch(Mockito.any()))
                .thenThrow(new DomainRuleViolation("homeTeam and awayTeam cannot be the same"));

        String json = """
            {
              "title": "TestMatch",
              "startAt": "%s",
              "leagueId": 20,
              "venueId": 30,
              "homeTeamId": 10,
              "awayTeamId": 10
            }
            """.formatted(Instant.now().plusSeconds(3600).toString());

        mvc.perform(
                        post("/matches")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }
}

