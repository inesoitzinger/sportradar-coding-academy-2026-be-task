package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query(value = """
        SELECT m.*
        FROM matches m
        JOIN leagues l ON l.id = m.league_id
        WHERE (:sportId IS NULL OR l.sport_id = :sportId)
          AND (:status IS NULL OR m.status = :status)
          AND (:venueId IS NULL OR m.venue_id = :venueId)
          AND (:leagueId IS NULL OR m.league_id = :leagueId)
          AND (:teamId IS NULL OR m.home_team_id = :teamId OR m.away_team_id = :teamId)
    """, nativeQuery = true)
    List<Match> searchDynamic(
            Long sportId,
            String status,
            Long venueId,
            Long leagueId,
            Long teamId
    );

    @Query(value = """
        SELECT DISTINCT m.*
        FROM matches m
        JOIN leagues l ON l.id = m.league_id
        JOIN sports s ON s.id = l.sport_id
        JOIN teams ht ON ht.id = m.home_team_id
        JOIN teams at ON at.id = m.away_team_id
        JOIN venues v ON v.id = m.venue_id
        WHERE lower(m.title) LIKE lower(concat('%', :q, '%'))
           OR lower(ht.name) LIKE lower(concat('%', :q, '%'))
           OR lower(at.name) LIKE lower(concat('%', :q, '%'))
           OR lower(l.name) LIKE lower(concat('%', :q, '%'))
           OR lower(s.name) LIKE lower(concat('%', :q, '%'))
           OR lower(v.name) LIKE lower(concat('%', :q, '%'))
    """, nativeQuery = true)
    List<Match> searchByText(String q);




}
