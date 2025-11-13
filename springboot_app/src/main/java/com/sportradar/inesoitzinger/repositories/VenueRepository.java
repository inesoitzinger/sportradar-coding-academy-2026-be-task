package com.sportradar.inesoitzinger.repositories;

import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {}
