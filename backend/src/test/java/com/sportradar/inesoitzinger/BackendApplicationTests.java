package com.sportradar.inesoitzinger;

import com.sportradar.inesoitzinger.repositories.MatchRepository;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import com.sportradar.inesoitzinger.repositories.TeamRepository;
import com.sportradar.inesoitzinger.repositories.VenueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    SportRepository sportRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    MatchRepository matchRepository;

    @Test
    void can_fetch_sports() {
        var sports = sportRepository.findAll();
        assertThat(sports).isNotEmpty();

        sports.forEach(s ->
                System.out.println(s.getId() + " - " + s.getName())
        );
    }

    @Test
    void can_fetch_teams() {
        var teams = teamRepository.findAll();
        assertThat(teams).isNotEmpty();

        teams.forEach(t ->
                System.out.println(t.getId() + " - " + t.getName())
        );
    }

    @Test
    void can_fetch_venues() {
        var venues = venueRepository.findAll();
        assertThat(venues).isNotEmpty();

        venues.forEach(v ->
                System.out.println(v.getId() + " - " + v.getName())
        );
    }

    @Test
    void can_fetch_matches() {
        var matches = matchRepository.findAll();
        assertThat(matches).isNotEmpty();

        matches.forEach(m ->
                System.out.println(m.getId()
                        + " - " + m.getHomeTeam().getName()
                        + " - " + m.getAwayTeam().getName()
        ));
    }

}
