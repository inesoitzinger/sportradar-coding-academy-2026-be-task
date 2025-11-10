package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.CreateMatchRequestDto;
import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.models.Match;
import com.sportradar.inesoitzinger.services.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final DtoMapper mapper;


    @GetMapping
    public List<MatchDto> search(@RequestParam Map<String,String> params) {
        return matchService.search(params)
                .stream()
                .map(m -> mapper.toMatchDto(m, matchService.calcHomeWinProbability(m)))
                .toList();
    }


    @GetMapping("/{id}")
    public MatchDto getOne(@PathVariable long id) {
        return matchService.getDtoById(id);
    }


    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CreateMatchRequestDto req) {
        return ResponseEntity.ok(matchService.createMatch(req).getId());
    }


}
