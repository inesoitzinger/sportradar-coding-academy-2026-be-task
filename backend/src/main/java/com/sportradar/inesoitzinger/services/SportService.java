package com.sportradar.inesoitzinger.services;

import com.sportradar.inesoitzinger.models.Sport;
import com.sportradar.inesoitzinger.repositories.SportRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SportService {
    private final SportRepository sportRepository;

    public List<Sport> findAll() {
        return sportRepository.findAll();
    }

    public Sport getById(long id) {
        return sportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sport " + id + " not found"));
    }

}
