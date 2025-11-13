package com.sportradar.inesoitzinger;

import com.sportradar.inesoitzinger.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FetchTablesTest {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Test
    void can_fetch_sports() {
        var sports = sportRepository.findAll();
        assertThat(sports).isNotEmpty();

        sports.forEach(s ->
                System.out.println(s.getId() + " - " + s.getName())
        );
    }

    @Test
    void can_fetch_leagues(){
        var leagues = leagueRepository.findAll();
        assertThat(leagues).isNotEmpty();

        leagues.forEach( l ->
                System.out.println(l.getId() + " - " + l.getName())
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
                System.out.println(m.getId() + " - " + m.getTitle())
        );
    }

}
