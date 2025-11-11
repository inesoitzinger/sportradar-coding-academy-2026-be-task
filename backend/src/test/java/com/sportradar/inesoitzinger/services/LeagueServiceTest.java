package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import com.sportradar.inesoitzinger.models.League;
import com.sportradar.inesoitzinger.models.Sport;
import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.repositories.LeagueRepository;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeagueServiceTest {

    LeagueRepository leagueRepository = mock(LeagueRepository.class);
    SportRepository sportRepository = mock(SportRepository.class);
    TeamRepository teamRepository = mock(TeamRepository.class);

    LeagueService service = new LeagueService(
            leagueRepository,
            sportRepository,
            teamRepository
    );

    @Test
    void findAll_returnsList() {
        League l = new League();
        l.setId(1L);

        when(leagueRepository.findAll()).thenReturn(List.of(l));

        assertThat(service.findAll()).containsExactly(l);
    }

    @Test
    void getById_returnsEntity() {
        League l = new League();
        l.setId(5L);

        when(leagueRepository.findById(5L)).thenReturn(Optional.of(l));

        assertThat(service.getById(5L)).isEqualTo(l);
    }

    @Test
    void getById_notFound_throws() {
        when(leagueRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9L))
                .isInstanceOf(DomainRuleViolation.class)
                .hasMessageContaining("league not found");
    }

    @Test
    void getBySportId_checksSportExistsFirst() {
        Sport s = new Sport();
        s.setId(2L);

        when(sportRepository.findById(2L)).thenReturn(Optional.of(s));
        when(leagueRepository.findBySportId(2L)).thenReturn(List.of());

        service.getBySportId(2L);

        verify(leagueRepository).findBySportId(2L);
    }

    @Test
    void getByTeamId_checksTeamExistsFirst() {
        Team t = new Team();
        t.setId(3L);

        when(teamRepository.findById(3L)).thenReturn(Optional.of(t));
        when(leagueRepository.findByTeamId(3L)).thenReturn(List.of());

        service.getByTeamId(3L);

        verify(leagueRepository).findByTeamId(3L);
    }
}
