package com.jsp.lostAndFound.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ComplaintRequestDTO {

    @NotNull(message = "Recovery request ID is required")
    @Positive(message = "Recovery request ID must be positive")
    private Long recoveryRequestId;

    @NotBlank(message = "Reason is required")
    @Size(max = 100, message = "Reason must not exceed 100 characters")
    private String reason;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    public ComplaintRequestDTO() {
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
}