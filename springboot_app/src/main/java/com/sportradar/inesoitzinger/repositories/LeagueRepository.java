package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Long> {

    @Query(value = """
        SELECT l.*
        FROM leagues l
        WHERE l.sport_id = :sportId
    """, nativeQuery = true)
    List<League> findBySportId(long sportId);



    @Query(value = """
        SELECT l.*
        FROM leagues l
        JOIN team_leagues tl ON tl.league_id = l.id
        WHERE tl.team_id = :teamId
    """, nativeQuery = true)
    List<League> findByTeamId(long teamId);





}
