package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.enums.MatchStatus;
import com.sportradar.inesoitzinger.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatus(MatchStatus status);

    List<Match> findByStartAtAfter(Instant instant);

    @Query(value = """
        select *
        from matches
        where league_id = :leagueId
    """, nativeQuery = true)
    List<Match> findByLeagueId(long leagueId);

    @Query(value = """
        select m.*
        from matches m
        join leagues l on l.id = m.league_id
        where l.sport_id = :sportId
    """, nativeQuery = true)
    List<Match> findBySportId(long sportId);

    @Query(value = """
        select m.*
        from matches m
        where m.home_team_id = :teamId
           or m.away_team_id = :teamId
    """, nativeQuery = true)
        List<Match> findByTeamId(long teamId);


    @Query(value = """
        select *
        from matches
        where venue_id = :venueId
    """, nativeQuery = true)
    List<Match> findByVenueId(long venueId);



}
