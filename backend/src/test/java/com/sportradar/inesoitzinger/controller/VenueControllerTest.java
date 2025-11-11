package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.VenueDto;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.exceptions.NotFoundException;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Venue;
import com.sportradar.inesoitzinger.services.VenueService;
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

@WebMvcTest(VenueController.class)
class VenueControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    VenueService venueService;

    @MockBean
    DtoMapper mapper;

    @Test
    void getAll_returnsList() throws Exception {
        Venue v = new Venue();
        v.setId(1L);
        v.setName("Allianz Arena");

        Mockito.when(venueService.findAll()).thenReturn(List.of(v));
        Mockito.when(mapper.toVenueDto(v))
                .thenReturn(new VenueDto(1L, "Allianz Arena", null, null));

        mvc.perform(get("/venues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Allianz Arena"));
    }

    @Test
    void getOne_returnsDto() throws Exception {
        Venue v = new Venue();
        v.setId(2L);
        v.setName("Signal Iduna Park");

        Mockito.when(venueService.getById(2L)).thenReturn(v);
        Mockito.when(mapper.toVenueDto(v))
                .thenReturn(new VenueDto(2L, "Signal Iduna Park", null, null));

        mvc.perform(get("/venues/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Signal Iduna Park"));
    }

    @Test
    void getOne_notFound_returns404() throws Exception {

        Mockito.when(venueService.getById(anyLong()))
                .thenThrow(new NotFoundException("venue not found"));

        mvc.perform(get("/venues/999"))
                .andExpect(status().isNotFound());
    }
}
