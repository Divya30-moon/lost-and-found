package com.jsp.lostAndFound.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "raised_by_id", nullable = false)
    private User raisedBy;

    @ManyToOne
    @JoinColumn(name = "recovery_request_id", nullable = false)
    private RecoveryRequest recoveryRequest;

    @Column(nullable = false, length = 100)
    private String reason;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @Column(length = 1000)
    private String adminResponse;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Complaint() {
    }

    public Complaint(User raisedBy, RecoveryRequest recoveryRequest,
                     String reason, String description,
                     ComplaintStatus status, String adminResponse,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.raisedBy = raisedBy;
        this.recoveryRequest = recoveryRequest;
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

    public User getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(User raisedBy) {
        this.raisedBy = raisedBy;
    }

    public RecoveryRequest getRecoveryRequest() {
        return recoveryRequest;
    }

    public void setRecoveryRequest(RecoveryRequest recoveryRequest) {
        this.recoveryRequest = recoveryRequest;
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