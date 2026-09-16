package com.jsp.lostAndFound.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.lostAndFound.dto.ComplaintRequestDTO;
import com.jsp.lostAndFound.dto.ComplaintResponseDTO;
import com.jsp.lostAndFound.entity.ComplaintStatus;
import com.jsp.lostAndFound.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
            @RequestParam Long userId,
            @Valid @RequestBody ComplaintRequestDTO requestDTO) {

        ComplaintResponseDTO response =
                complaintService.createComplaint(userId, requestDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<ComplaintResponseDTO> getComplaintById(
            @PathVariable Long complaintId,
            @RequestParam Long userId) {

        return ResponseEntity.ok(
                complaintService.getComplaintById(
                        complaintId, userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ComplaintResponseDTO>> getComplaintsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByUser(userId));
    }

    @GetMapping("/recovery-request/{recoveryRequestId}")
    public ResponseEntity<List<ComplaintResponseDTO>>
            getComplaintsByRecoveryRequest(
                    @PathVariable Long recoveryRequestId,
                    @RequestParam Long userId) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByRecoveryRequest(
                        recoveryRequestId, userId));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ComplaintResponseDTO>> getAllComplaints(
            @RequestParam Long adminId) {

        return ResponseEntity.ok(
                complaintService.getAllComplaints(adminId));
    }

    @GetMapping("/admin/status/{status}")
    public ResponseEntity<List<ComplaintResponseDTO>> getComplaintsByStatus(
            @PathVariable ComplaintStatus status,
            @RequestParam Long adminId) {

        return ResponseEntity.ok(
                complaintService.getComplaintsByStatus(
                        adminId, status));
    }

    @PutMapping("/{complaintId}/status")
    public ResponseEntity<ComplaintResponseDTO> updateComplaintStatus(
            @PathVariable Long complaintId,
            @RequestParam Long adminId,
            @RequestParam ComplaintStatus status,
            @RequestParam(required = false) String adminResponse) {

        return ResponseEntity.ok(
                complaintService.updateComplaintStatus(
                        complaintId,
                        adminId,
                        status,
                        adminResponse));
    }
}