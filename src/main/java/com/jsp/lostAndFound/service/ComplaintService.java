package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.ComplaintRequestDTO;
import com.jsp.lostAndFound.dto.ComplaintResponseDTO;
import com.jsp.lostAndFound.entity.ComplaintStatus;

public interface ComplaintService {

    ComplaintResponseDTO createComplaint(
            Long userId,
            ComplaintRequestDTO requestDTO);

    ComplaintResponseDTO getComplaintById(
            Long complaintId,
            Long userId);

    List<ComplaintResponseDTO> getComplaintsByUser(
            Long userId);

    List<ComplaintResponseDTO> getComplaintsByRecoveryRequest(
            Long recoveryRequestId,
            Long userId);

    List<ComplaintResponseDTO> getAllComplaints(
            Long adminId);

    List<ComplaintResponseDTO> getComplaintsByStatus(
            Long adminId,
            ComplaintStatus status);

    ComplaintResponseDTO updateComplaintStatus(
            Long complaintId,
            Long adminId,
            ComplaintStatus newStatus,
            String adminResponse);
}