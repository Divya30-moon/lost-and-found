package com.jsp.lostAndFound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.ItemSearchRequestDTO;
import com.jsp.lostAndFound.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> searchItems(
            @ModelAttribute ItemSearchRequestDTO request) {

        return ResponseEntity.ok(
                searchService.searchItems(request)
        );
    }
}