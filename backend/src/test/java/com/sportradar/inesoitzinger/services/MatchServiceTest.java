package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchSearchDto;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.exceptions.NotFoundException;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.*;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    MatchRepository matchRepository = mock(MatchRepository.class);
    TeamRepository teamRepository = mock(TeamRepository.class);
    VenueRepository venueRepository = mock(VenueRepository.class);
    LeagueRepository leagueRepository = mock(LeagueRepository.class);

    MatchService service = new MatchService(
            matchRepository,
            teamRepository,
            venueRepository,
            leagueRepository,
            null
    );


    @Test
    void getById_returnsMatch() {
        Match m = new Match();
        m.setId(5L);

        when(matchRepository.findById(5L)).thenReturn(Optional.of(m));

        assertThat(service.getById(5L)).isEqualTo(m);
    }

    @Test
    void getById_notFound_throws() {
        when(matchRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("match not found");
    }

    @Test
    void search_withQ_callsSearchByText() {
        MatchSearchDto dto = new MatchSearchDto("foo", null, null, null, null, null);

        when(matchRepository.searchByText("foo")).thenReturn(List.of());

        service.search(dto);

        verify(matchRepository).searchByText("foo");
        verifyNoMoreInteractions(matchRepository);
    }

    @Test
    void createMatch_callsSave() {
        Team home = new Team(); home.setId(1L);
        Team away = new Team(); away.setId(2L);
        League league = new League(); league.setId(3L);
        Venue venue = new Venue(); venue.setId(4L);

        // <<< FIX START >>>
        var sport = new Sport();
        sport.setId(111L);

        home.setSport(sport);
        away.setSport(sport);
        league.setSport(sport);
        // <<< FIX END >>>

        when(teamRepository.getReferenceById(1L)).thenReturn(home);
        when(teamRepository.getReferenceById(2L)).thenReturn(away);
        when(leagueRepository.getReferenceById(3L)).thenReturn(league);
        when(venueRepository.getReferenceById(4L)).thenReturn(venue);

        Match saved = new Match();
        saved.setId(99L);
        when(matchRepository.save(any())).thenReturn(saved);

        var req = new CreateMatchRequestDto(
                "Test", Instant.now().plusSeconds(3600),
                3L, 4L, 1L, 2L
        );

        var result = service.createMatch(req);

        assertThat(result.getId()).isEqualTo(99L);
        verify(matchRepository).save(any(Match.class));
    }

}
