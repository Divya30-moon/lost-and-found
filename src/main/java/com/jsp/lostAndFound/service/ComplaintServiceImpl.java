package com.jsp.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.ComplaintRequestDTO;
import com.jsp.lostAndFound.dto.ComplaintResponseDTO;
import com.jsp.lostAndFound.entity.Complaint;
import com.jsp.lostAndFound.entity.ComplaintStatus;
import com.jsp.lostAndFound.entity.NotificationType;
import com.jsp.lostAndFound.entity.RecoveryRequest;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.repository.ComplaintRepository;
import com.jsp.lostAndFound.repository.RecoveryRequestRepository;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final RecoveryRequestRepository recoveryRequestRepository;
    private final NotificationService notificationService;

    public ComplaintServiceImpl(
            ComplaintRepository complaintRepository,
            UserRepository userRepository,
            RecoveryRequestRepository recoveryRequestRepository,
            NotificationService notificationService) {

        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.recoveryRequestRepository = recoveryRequestRepository;
        this.notificationService = notificationService;
    }

    @Override
    public ComplaintResponseDTO createComplaint(
            Long userId,
            ComplaintRequestDTO requestDTO) {

        User user = getUser(userId);

        RecoveryRequest recoveryRequest =
                recoveryRequestRepository.findById(
                        requestDTO.getRecoveryRequestId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Recovery request not found"));

        validateParticipant(userId, recoveryRequest);

        Complaint complaint = new Complaint();

        complaint.setRaisedBy(user);
        complaint.setRecoveryRequest(recoveryRequest);
        complaint.setReason(requestDTO.getReason().trim());
        complaint.setDescription(requestDTO.getDescription().trim());

        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setAdminResponse(null);

        LocalDateTime now = LocalDateTime.now();

        complaint.setCreatedAt(now);
        complaint.setUpdatedAt(now);

        Complaint savedComplaint =
                complaintRepository.save(complaint);

        return convertToResponseDTO(savedComplaint);
    }

    @Override
    public ComplaintResponseDTO getComplaintById(
            Long complaintId,
            Long userId) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Complaint not found"));

        validateParticipant(
                userId,
                complaint.getRecoveryRequest());

        return convertToResponseDTO(complaint);
    }

    @Override
    public List<ComplaintResponseDTO> getComplaintsByUser(
            Long userId) {

        getUser(userId);

        return complaintRepository
                .findByRaisedById(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ComplaintResponseDTO> getComplaintsByRecoveryRequest(
            Long recoveryRequestId,
            Long userId) {

        RecoveryRequest recoveryRequest =
                recoveryRequestRepository.findById(recoveryRequestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Recovery request not found"));

        validateParticipant(userId, recoveryRequest);

        return complaintRepository
                .findByRecoveryRequestId(recoveryRequestId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ComplaintResponseDTO> getAllComplaints(
            Long adminId) {

        validateAdmin(adminId);

        return complaintRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ComplaintResponseDTO> getComplaintsByStatus(
            Long adminId,
            ComplaintStatus status) {

        validateAdmin(adminId);

        if (status == null) {
            throw new IllegalArgumentException(
                    "Complaint status is required");
        }

        return complaintRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public ComplaintResponseDTO updateComplaintStatus(
            Long complaintId,
            Long adminId,
            ComplaintStatus newStatus,
            String adminResponse) {

        validateAdmin(adminId);

        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "New complaint status is required");
        }

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Complaint not found"));

        validateStatusTransition(
                complaint.getStatus(),
                newStatus);

        if (newStatus == ComplaintStatus.RESOLVED
                || newStatus == ComplaintStatus.REJECTED) {

            if (adminResponse == null
                    || adminResponse.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Admin response is required when resolving or rejecting a complaint");
            }

            complaint.setAdminResponse(
                    adminResponse.trim());
        }

        complaint.setStatus(newStatus);
        complaint.setUpdatedAt(LocalDateTime.now());

        Complaint updatedComplaint =
                complaintRepository.save(complaint);

        sendComplaintNotification(
                updatedComplaint,
                newStatus);

        return convertToResponseDTO(updatedComplaint);
    }

    private void sendComplaintNotification(
            Complaint complaint,
            ComplaintStatus status) {

        if (status == ComplaintStatus.RESOLVED) {

            notificationService.createNotification(
                    complaint.getRaisedBy().getId(),
                    "Your complaint has been resolved by the admin.",
                    NotificationType.COMPLAINT_RESOLVED
            );

        } else if (status == ComplaintStatus.REJECTED) {

            notificationService.createNotification(
                    complaint.getRaisedBy().getId(),
                    "Your complaint has been rejected by the admin.",
                    NotificationType.COMPLAINT_REJECTED
            );
        }
    }

    private User getUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"));
    }

    private void validateParticipant(
            Long userId,
            RecoveryRequest recoveryRequest) {

        Long ownerId =
                recoveryRequest.getLostItem()
                        .getReportedBy()
                        .getId();

        Long finderId =
                recoveryRequest.getFoundItem()
                        .getReportedBy()
                        .getId();

        if (!userId.equals(ownerId)
                && !userId.equals(finderId)) {

            throw new IllegalArgumentException(
                    "Only participants of the recovery request can access this complaint");
        }
    }

    private void validateAdmin(Long adminId) {

        User admin = getUser(adminId);

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {

            throw new IllegalArgumentException(
                    "Only admin users can perform this operation");
        }
    }

    private void validateStatusTransition(
            ComplaintStatus currentStatus,
            ComplaintStatus newStatus) {

        if (currentStatus == ComplaintStatus.OPEN
                && newStatus == ComplaintStatus.UNDER_REVIEW) {
            return;
        }

        if (currentStatus == ComplaintStatus.UNDER_REVIEW
                && (newStatus == ComplaintStatus.RESOLVED
                || newStatus == ComplaintStatus.REJECTED)) {
            return;
        }

        throw new IllegalArgumentException(
                "Invalid complaint status transition from "
                + currentStatus + " to " + newStatus);
    }

    private ComplaintResponseDTO convertToResponseDTO(
            Complaint complaint) {

        User raisedBy = complaint.getRaisedBy();

        return new ComplaintResponseDTO(
                complaint.getId(),
                raisedBy.getId(),
                raisedBy.getName(),
                complaint.getRecoveryRequest().getId(),
                complaint.getReason(),
                complaint.getDescription(),
                complaint.getStatus(),
                complaint.getAdminResponse(),
                complaint.getCreatedAt(),
                complaint.getUpdatedAt()
        );
    }
}