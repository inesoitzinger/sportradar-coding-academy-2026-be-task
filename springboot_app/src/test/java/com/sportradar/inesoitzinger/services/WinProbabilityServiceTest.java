package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.models.Team;
import com.sportradar.inesoitzinger.repositories.MatchRepository;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class WinProbabilityServiceTest {

    MatchRepository repo = mock(MatchRepository.class);

    WinProbabilityService service = new WinProbabilityService(repo);

    private Match scheduledMatchWithHomeTeamId(long teamId) {
        Match m = new Match();
        m.setStatus(MatchStatus.scheduled);
        Team t = new Team();
        t.setId(teamId);
        m.setHomeTeam(t);
        return m;
    }

    @Test
    void returnsNull_whenStatusNotScheduled() {
        Match m = new Match();
        m.setStatus(MatchStatus.finished);

        assertThat(service.calcHome(m)).isNull();
    }

    @Test
    void returnsNull_whenZeroMatches() {
        Match m = scheduledMatchWithHomeTeamId(1L);

        when(repo.getRecord(1L)).thenReturn(Map.of("wins", 0L, "matches", 0L));

        assertThat(service.calcHome(m)).isNull();
    }

    @Test
    void returnsProbability() {
        Match m = scheduledMatchWithHomeTeamId(2L);

        when(repo.getRecord(2L)).thenReturn(Map.of("wins", 7L, "matches", 10L));

        assertThat(service.calcHome(m)).isEqualTo(0.7);
    }

    @Test
    void usesCorrectHomeTeamId() {
        Match m = scheduledMatchWithHomeTeamId(42L);

        when(repo.getRecord(42L)).thenReturn(Map.of("wins", 1L, "matches", 1L));

        service.calcHome(m);

        verify(repo).getRecord(42L);
    }
}
