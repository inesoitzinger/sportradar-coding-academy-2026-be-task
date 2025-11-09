package com.sportradar.inesoitzinger.controller;

import com.sportradar.inesoitzinger.dtos.MatchDto;
import com.sportradar.inesoitzinger.mappers.DtoMapper;
import com.sportradar.inesoitzinger.services.MatchService;
import lombok.RequiredArgsConstructor;
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
                .map(mapper::toMatchDto)
                .toList();
    }

    @GetMapping("/{id}")
    public MatchDto getOne(@PathVariable long id) {
        return mapper.toMatchDto(matchService.getById(id));
    }
}
