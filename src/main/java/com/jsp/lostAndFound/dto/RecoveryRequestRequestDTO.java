package com.jsp.lostAndFound.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class RecoveryRequestRequestDTO {

    @NotNull(message = "Lost item id is required")
    @Positive(message = "Lost item id must be positive")
    private Long lostItemId;

    @NotNull(message = "Found item id is required")
    @Positive(message = "Found item id must be positive")
    private Long foundItemId;

    @NotBlank(message = "Message is required")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    private String message;

    public RecoveryRequestRequestDTO() {
    }

    public RecoveryRequestRequestDTO(Long lostItemId,
                                     Long foundItemId,
                                     String message) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.message = message;
    }

    public Long getLostItemId() {
        return lostItemId;
    }

    public void setLostItemId(Long lostItemId) {
        this.lostItemId = lostItemId;
    }

    public Long getFoundItemId() {
        return foundItemId;
    }

    public void setFoundItemId(Long foundItemId) {
        this.foundItemId = foundItemId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}