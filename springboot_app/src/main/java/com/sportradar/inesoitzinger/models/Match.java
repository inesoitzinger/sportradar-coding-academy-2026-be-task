package com.sportradar.inesoitzinger.models;

import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.exceptions.DomainRuleViolation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private  League league;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

    public static Match schedule(
            String title,
            Instant startAt,
            Team home,
            Team away,
            League league,
            Venue venue
    ) {

        if (home.getId().equals(away.getId()))
            throw new DomainRuleViolation("homeTeam and awayTeam cannot be the same");

        // domain rule aus deinem Modell: Sport muss matchen
        if (!home.getSport().equals(league.getSport()) ||
                !away.getSport().equals(league.getSport()))
            throw new DomainRuleViolation("both teams must match the league's sport");

        if (startAt.isBefore(Instant.now()))
            throw new DomainRuleViolation("startAt must be in the future");

        Match m = new Match();
        m.setTitle(title);
        m.setStartAt(startAt);
        m.setStatus(MatchStatus.scheduled);
        m.setHomeTeam(home);
        m.setAwayTeam(away);
        m.setLeague(league);
        m.setVenue(venue);

        return m;
    }




}
