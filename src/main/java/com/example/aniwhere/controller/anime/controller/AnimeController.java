package com.example.aniwhere.controller.anime.controller;

import com.example.aniwhere.domain.anime.dto.AnimeDTO.*;
import com.example.aniwhere.service.anime.service.AnimeService;
import com.example.aniwhere.global.error.ErrorCode;
import com.example.aniwhere.global.error.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AnimeController {

    private final AnimeService animeService;


    @GetMapping("/anime/{id}")
    public ResponseEntity<AnimeResponseDTO> getAnimeById(@PathVariable int id) {
        AnimeResponseDTO animeResponse = animeService.getAnimeById(id);
        return ResponseEntity.ok(animeResponse);
    }

    /**
     *
     * @return WeekdayAnimeDTO
     */
    @Operation(
            summary = "요일별 애니메이션 조회",
            description = "요일별 애니메이션을 월요일(1) ~ 일요일(7) 순으로 조회합니다."
    )
    @GetMapping("/anime/weekday")
    public ResponseEntity<Map<Integer, List<WeekdayAnimeDTO>>> getWeekdayAnimeList(){
        Map<Integer, List<WeekdayAnimeDTO>> animeResponse = animeService.getAnimeWeekdayList();
        return ResponseEntity.ok(animeResponse);
    }

}
