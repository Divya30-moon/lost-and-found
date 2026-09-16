package com.jsp.lostAndFound.dto;

import java.time.LocalDateTime;

import com.jsp.lostAndFound.entity.ComplaintStatus;

public class ComplaintResponseDTO {

    private Long id;

    private Long raisedById;
    private String raisedByName;

    private Long recoveryRequestId;

    private String reason;
    private String description;

    private ComplaintStatus status;

    private String adminResponse;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ComplaintResponseDTO() {
    }

    public ComplaintResponseDTO(Long id,
                                Long raisedById,
                                String raisedByName,
                                Long recoveryRequestId,
                                String reason,
                                String description,
                                ComplaintStatus status,
                                String adminResponse,
                                LocalDateTime createdAt,
                                LocalDateTime updatedAt) {

        this.id = id;
        this.raisedById = raisedById;
        this.raisedByName = raisedByName;
        this.recoveryRequestId = recoveryRequestId;
        this.reason = reason;
        this.description = description;
        this.status = status;
        this.adminResponse = adminResponse;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRaisedById() {
        return raisedById;
    }

    public void setRaisedById(Long raisedById) {
        this.raisedById = raisedById;
    }

    public String getRaisedByName() {
        return raisedByName;
    }

    public void setRaisedByName(String raisedByName) {
        this.raisedByName = raisedByName;
    }

    public Long getRecoveryRequestId() {
        return recoveryRequestId;
    }

    public void setRecoveryRequestId(Long recoveryRequestId) {
        this.recoveryRequestId = recoveryRequestId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}