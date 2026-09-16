package com.jsp.lostAndFound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.lostAndFound.dto.MatchResponseDTO;
import com.jsp.lostAndFound.service.MatchingService;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/{lostItemId}")
    public ResponseEntity<List<MatchResponseDTO>> findMatches(
            @PathVariable Long lostItemId) {

        return ResponseEntity.ok(
                matchingService.findMatches(lostItemId)
        );
    }
}