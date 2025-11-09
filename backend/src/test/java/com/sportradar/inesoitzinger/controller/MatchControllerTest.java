package com.sportradar.inesoitzinger;

import com.sportradar.inesoitzinger.controller.MatchController;
import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.services.MatchService;
import com.sportradar.inesoitzinger.mappers.DtoMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
class MatchControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    MatchService matchService;

    @MockBean
    DtoMapper mapper;

    @Test
    void getMatches() throws Exception {
        mvc.perform(get("/matches"))
                .andExpect(status().isOk());
    }

    @Test
    void getMatchesByStatusScheduled() throws Exception {
        mvc.perform(get("/matches").param("status", "scheduled"))
                .andExpect(status().isOk());

        Mockito.verify(matchService).getByStatus(Mockito.eq(MatchStatus.scheduled));
    }

    @Test
    void getMatchesByStatusFinished() throws Exception {
        mvc.perform(get("/matches").param("status", "finished"))
                .andExpect(status().isOk());

        Mockito.verify(matchService).getByStatus(Mockito.eq(MatchStatus.finished));
    }

    @Test
    void getMatchesByStatusLive() throws Exception {
        mvc.perform(get("/matches").param("status", "live"))
                .andExpect(status().isOk());

        Mockito.verify(matchService).getByStatus(Mockito.eq(MatchStatus.live));
    }


}
