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

class TeamServiceTest {

    TeamRepository teamRepo = mock(TeamRepository.class);
    LeagueRepository leagueRepo = mock(LeagueRepository.class);
    SportRepository sportRepo = mock(SportRepository.class);

    TeamService service = new TeamService(teamRepo, leagueRepo, sportRepo);

    @Test
    void findAll_returnsList() {
        Team t = new Team();
        t.setId(1L);

        when(teamRepo.findAll()).thenReturn(List.of(t));

        assertThat(service.findAll()).containsExactly(t);
    }

    @Test
    void getById_returnsEntity() {
        Team t = new Team();
        t.setId(5L);

        when(teamRepo.findById(5L)).thenReturn(Optional.of(t));

        assertThat(service.getById(5L)).isEqualTo(t);
    }

    @Test
    void getById_notFound_throws() {
        when(teamRepo.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(9L))
                .isInstanceOf(DomainRuleViolation.class);
    }

    @Test
    void getBySportId_checksSportExistsFirst() {
        Sport s = new Sport(); s.setId(2L);

        when(sportRepo.findById(2L)).thenReturn(Optional.of(s));
        when(teamRepo.findBySportId(2L)).thenReturn(List.of());

        service.getBySportId(2L);

        verify(teamRepo).findBySportId(2L);
    }

    @Test
    void getByLeagueId_returnsListDirect() {
        when(teamRepo.findByLeagueId(33L)).thenReturn(List.of());

        service.getByLeagueId(33L);

        verify(teamRepo).findByLeagueId(33L);
    }

    @Test
    void getTeamsForLeague_checksLeagueExistsFirst() {
        League l = new League(); l.setId(44L);

        when(leagueRepo.findById(44L)).thenReturn(Optional.of(l));
        when(teamRepo.findByLeagueId(44L)).thenReturn(List.of());

        service.getTeamsForLeague(44L);

        verify(teamRepo).findByLeagueId(44L);
    }
}
