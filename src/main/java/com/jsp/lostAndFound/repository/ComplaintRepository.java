package com.jsp.lostAndFound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.lostAndFound.entity.Complaint;
import com.jsp.lostAndFound.entity.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByRaisedById(Long userId);

    List<Complaint> findByRecoveryRequestId(Long recoveryRequestId);

    List<Complaint> findByStatus(ComplaintStatus status);
}