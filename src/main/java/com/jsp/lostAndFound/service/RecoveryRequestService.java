package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.RecoveryRequestRequestDTO;
import com.jsp.lostAndFound.dto.RecoveryRequestResponseDTO;

public interface RecoveryRequestService {

    RecoveryRequestResponseDTO createRecoveryRequest(
            Long requesterId,
            RecoveryRequestRequestDTO requestDTO
    );

    RecoveryRequestResponseDTO getRecoveryRequestById(
            Long requestId,
            Long requesterId
    );

    List<RecoveryRequestResponseDTO> getRequestsForLostItem(
            Long lostItemId,
            Long requesterId
    );

    List<RecoveryRequestResponseDTO> getRequestsForFoundItem(
            Long foundItemId,
            Long requesterId
    );
    
    RecoveryRequestResponseDTO acceptRequest(
            Long requestId, Long finderId);

    RecoveryRequestResponseDTO rejectRequest(
            Long requestId, Long finderId);

    RecoveryRequestResponseDTO cancelRequest(
            Long requestId, Long ownerId);

    RecoveryRequestResponseDTO markAsReturned(
            Long requestId, Long finderId);

    RecoveryRequestResponseDTO completeRequest(
            Long requestId, Long ownerId);
}