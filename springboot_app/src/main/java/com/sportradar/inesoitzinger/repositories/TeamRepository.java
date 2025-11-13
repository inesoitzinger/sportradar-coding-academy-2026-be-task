package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = """
        select t.*
        from teams t
        join team_leagues tl on tl.team_id = t.id
        join leagues l on l.id = tl.league_id
        where l.sport_id = :sportId
    """, nativeQuery = true)
    List<Team> findBySportId(long sportId);

    @Query(value = """
        select t.*
        from teams t
        join team_leagues tl on tl.team_id = t.id
        where tl.league_id = :leagueId
    """, nativeQuery = true)
        List<Team> findByLeagueId(long leagueId);


}
