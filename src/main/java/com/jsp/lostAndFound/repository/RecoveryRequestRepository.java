package com.jsp.lostAndFound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.lostAndFound.entity.RecoveryRequest;
import com.jsp.lostAndFound.entity.RecoveryRequestStatus;

public interface RecoveryRequestRepository
        extends JpaRepository<RecoveryRequest, Long> {

    boolean existsByLostItemIdAndFoundItemIdAndStatusIn(
            Long lostItemId,
            Long foundItemId,
            List<RecoveryRequestStatus> statuses
    );

    boolean existsByLostItemIdAndStatusIn(
            Long lostItemId,
            List<RecoveryRequestStatus> statuses
    );

    List<RecoveryRequest> findByLostItemId(Long lostItemId);

    List<RecoveryRequest> findByFoundItemId(Long foundItemId);

    List<RecoveryRequest> findByStatus(RecoveryRequestStatus status);
}