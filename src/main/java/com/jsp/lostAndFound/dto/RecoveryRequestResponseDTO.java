package com.jsp.lostAndFound.dto;

import java.time.LocalDateTime;

import com.jsp.lostAndFound.entity.RecoveryRequestStatus;

public class RecoveryRequestResponseDTO {

    private Long id;

    private Long lostItemId;
    private String lostItemTitle;

    private Long foundItemId;
    private String foundItemTitle;

    private Long ownerId;
    private String ownerName;

    private Long finderId;
    private String finderName;

    private RecoveryRequestStatus status;

    private String message;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecoveryRequestResponseDTO() {
    }

    public RecoveryRequestResponseDTO(
            Long id,
            Long lostItemId,
            String lostItemTitle,
            Long foundItemId,
            String foundItemTitle,
            Long ownerId,
            String ownerName,
            Long finderId,
            String finderName,
            RecoveryRequestStatus status,
            String message,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.lostItemId = lostItemId;
        this.lostItemTitle = lostItemTitle;
        this.foundItemId = foundItemId;
        this.foundItemTitle = foundItemTitle;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.finderId = finderId;
        this.finderName = finderName;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }

    public String getLostItemTitle() {
        return lostItemTitle;
    }

    public void setLostItemTitle(String lostItemTitle) {
        this.lostItemTitle = lostItemTitle;
    }

    public Long getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(Long foundItemId) {
        this.foundItemId = foundItemId;
    }

    public String getFoundItemTitle() {
        return foundItemTitle;
    }

    public void setFoundItemTitle(String foundItemTitle) {
        this.foundItemTitle = foundItemTitle;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getFinderId() {
        return finderId;
    }

    public void setFinderId(Long finderId) {
        this.finderId = finderId;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public RecoveryRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RecoveryRequestStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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