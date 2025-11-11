package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.Venue;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class VenueServiceTest {

    VenueRepository venueRepo = mock(VenueRepository.class);
    LeagueRepository leagueRepo = mock(LeagueRepository.class);

    VenueService service = new VenueService(venueRepo, leagueRepo);

    @Test
    void findAll_returnsList() {
        Venue v = new Venue();
        v.setId(1L);

        when(venueRepo.findAll()).thenReturn(List.of(v));

        assertThat(service.findAll()).containsExactly(v);
    }

    @Test
    void getById_returnsVenue() {
        Venue v = new Venue();
        v.setId(5L);

        when(venueRepo.findById(5L)).thenReturn(Optional.of(v));

        assertThat(service.getById(5L)).isEqualTo(v);
    }

    @Test
    void getById_notFound_throws() {
        when(venueRepo.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(DomainRuleViolation.class);
    }
}
