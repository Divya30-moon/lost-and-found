package com.jsp.lostAndFound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.lostAndFound.dto.RecoveryRequestRequestDTO;
import com.jsp.lostAndFound.dto.RecoveryRequestResponseDTO;
import com.jsp.lostAndFound.service.RecoveryRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recovery-requests")
public class RecoveryRequestController {

    private final RecoveryRequestService recoveryRequestService;

    public RecoveryRequestController(RecoveryRequestService recoveryRequestService) {
        this.recoveryRequestService = recoveryRequestService;
    }

    // Create a recovery request
    @PostMapping
    public ResponseEntity<RecoveryRequestResponseDTO> createRecoveryRequest(
            @RequestParam Long requesterId,
            @Valid @RequestBody RecoveryRequestRequestDTO requestDTO) {

        return ResponseEntity.ok(
                recoveryRequestService.createRecoveryRequest(requesterId, requestDTO)
        );
    }

    // Get recovery request by ID
    @GetMapping("/{requestId}")
    public ResponseEntity<RecoveryRequestResponseDTO> getRecoveryRequestById(
            @PathVariable Long requestId,
            @RequestParam Long requesterId) {

        return ResponseEntity.ok(
                recoveryRequestService.getRecoveryRequestById(requestId, requesterId)
        );
    }

    // Get requests for a lost item
    @GetMapping("/lost-item/{lostItemId}")
    public ResponseEntity<List<RecoveryRequestResponseDTO>> getRequestsForLostItem(
            @PathVariable Long lostItemId,
            @RequestParam Long requesterId) {

        return ResponseEntity.ok(
                recoveryRequestService.getRequestsForLostItem(lostItemId, requesterId)
        );
    }

    // Get requests for a found item
    @GetMapping("/found-item/{foundItemId}")
    public ResponseEntity<List<RecoveryRequestResponseDTO>> getRequestsForFoundItem(
            @PathVariable Long foundItemId,
            @RequestParam Long requesterId) {

        return ResponseEntity.ok(
                recoveryRequestService.getRequestsForFoundItem(foundItemId, requesterId)
        );
    }

    // Finder accepts the recovery request
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<RecoveryRequestResponseDTO> acceptRequest(
            @PathVariable Long requestId,
            @RequestParam Long finderId) {

        return ResponseEntity.ok(
                recoveryRequestService.acceptRequest(requestId, finderId)
        );
    }

    // Finder rejects the recovery request
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<RecoveryRequestResponseDTO> rejectRequest(
            @PathVariable Long requestId,
            @RequestParam Long finderId) {

        return ResponseEntity.ok(
                recoveryRequestService.rejectRequest(requestId, finderId)
        );
    }

    // Owner cancels the recovery request
    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<RecoveryRequestResponseDTO> cancelRequest(
            @PathVariable Long requestId,
            @RequestParam Long ownerId) {

        return ResponseEntity.ok(
                recoveryRequestService.cancelRequest(requestId, ownerId)
        );
    }

    // Finder marks the item as returned
    @PutMapping("/{requestId}/returned")
    public ResponseEntity<RecoveryRequestResponseDTO> markAsReturned(
            @PathVariable Long requestId,
            @RequestParam Long finderId) {

        return ResponseEntity.ok(
                recoveryRequestService.markAsReturned(requestId, finderId)
        );
    }

    // Owner confirms recovery is completed
    @PutMapping("/{requestId}/complete")
    public ResponseEntity<RecoveryRequestResponseDTO> completeRequest(
            @PathVariable Long requestId,
            @RequestParam Long ownerId) {

        return ResponseEntity.ok(
                recoveryRequestService.completeRequest(requestId, ownerId)
        );
    }
}