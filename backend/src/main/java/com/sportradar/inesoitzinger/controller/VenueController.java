package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.VenueDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venues")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;
    private final DtoMapper mapper;

    /**
     * Returns all venues.
     *
     * @return list of venues
     */
    @GetMapping
    public List<VenueDto> getAll() {
        return venueService.findAll()
                .stream()
                .map(mapper::toVenueDto)
                .toList();
    }

    /**
     * Returns one venue by id.
     *
     * @param id venue id
     * @return venue details
     */
    @GetMapping("/{id}")
    public VenueDto getOne(@PathVariable long id) {
        return mapper.toVenueDto(venueService.getById(id));
    }

}
