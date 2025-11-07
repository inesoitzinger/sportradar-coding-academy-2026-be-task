package com.sportradar.inesoitzinger.models;

import com.sportradar.inesoitzinger.enums.MatchStatus;
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
    private Long homeScore;

    @Column(name = "away_score")
    private Long awayScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status;

}
