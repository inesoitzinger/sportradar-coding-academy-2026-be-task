package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Long> {
}
