package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
