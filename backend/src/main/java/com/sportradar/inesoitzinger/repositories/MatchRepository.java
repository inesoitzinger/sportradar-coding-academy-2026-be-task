package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
