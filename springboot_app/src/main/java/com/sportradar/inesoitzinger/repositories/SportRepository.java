package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}
