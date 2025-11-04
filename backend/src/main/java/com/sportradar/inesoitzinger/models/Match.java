package com.sportradar.inesoitzinger.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "home_score")
    private Long homeScore;

    @Column(name = "away_score")
    private Long awayScore;

    private String status;
}
