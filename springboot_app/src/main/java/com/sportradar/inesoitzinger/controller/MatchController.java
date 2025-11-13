package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.dtos.MatchSearchDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.MatchService;
import com.sportradar.inesoitzinger.services.WinProbabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final WinProbabilityService winProbabilityService;
    private final DtoMapper dtoMapper;

    /**
     * Returns a list of matches filtered by optional search parameters.
     * If parameter "q" is provided, a text search is executed.
     *
     * @param dto search parameters
     * @return list of matches
     */
    @GetMapping
    public List<MatchDto> search(@ModelAttribute MatchSearchDto dto) {
        var result = matchService.search(dto);
        return result.stream()
                .map(m -> dtoMapper.toMatchDto(m, winProbabilityService.calcHome(m)))
                .toList();
    }

    /**
     * Returns one match by id.
     *
     * @param id match id
     * @return match details
     */
    @GetMapping("/{id}")
    public MatchDto getOne(@PathVariable long id) {
        var m = matchService.getById(id);
        return dtoMapper.toMatchDto(m, winProbabilityService.calcHome(m));
    }

    /**
     * Creates a new match.
     * Domain rules are validated inside the Match aggregate.
     *
     * @param req create match request
     * @return id of created match
     */
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CreateMatchRequestDto req) {
        return ResponseEntity.ok(matchService.createMatch(req).getId());
    }

}
