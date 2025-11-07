package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}
