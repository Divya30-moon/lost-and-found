package com.jsp.lostAndFound.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsp.lostAndFound.dto.ItemRequestDTO;
import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.UpdateItemRequestDTO;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;
import com.jsp.lostAndFound.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(
            @Valid @RequestBody ItemRequestDTO dto) {

        return ResponseEntity.ok(
                itemService.createItem(dto)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(
            @PathVariable Long id) {

        ItemResponseDTO item = itemService.getItemById(id);

        return ResponseEntity.ok(item);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems() {

        List<ItemResponseDTO> items = itemService.getAllItems();

        return ResponseEntity.ok(items);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ItemResponseDTO>> getItemsByTypeAndStatus(
            @RequestParam ItemType itemType,
            @RequestParam ItemStatus status) {

        List<ItemResponseDTO> items =
                itemService.getItemsByTypeAndStatus(itemType, status);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemResponseDTO>> getItemsReportedByUser(
            @PathVariable Long userId) {

        List<ItemResponseDTO> items =
                itemService.getItemsReportedByUser(userId);

        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ItemResponseDTO>> getItemsByCategory(
            @PathVariable Long categoryId) {

        List<ItemResponseDTO> items =
                itemService.getItemsByCategory(categoryId);

        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem(
            @PathVariable Long id,
            @RequestBody @Valid UpdateItemRequestDTO updateItemRequestDTO) {

        ItemResponseDTO updatedItem =
                itemService.updateItem(id, updateItemRequestDTO);

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id) {

        itemService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }
}