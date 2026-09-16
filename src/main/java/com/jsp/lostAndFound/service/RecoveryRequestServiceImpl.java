package com.jsp.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsp.lostAndFound.dto.RecoveryRequestRequestDTO;
import com.jsp.lostAndFound.dto.RecoveryRequestResponseDTO;
import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;
import com.jsp.lostAndFound.entity.NotificationType;
import com.jsp.lostAndFound.entity.RecoveryRequest;
import com.jsp.lostAndFound.entity.RecoveryRequestStatus;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.exception.ItemNotFoundException;
import com.jsp.lostAndFound.exception.UserNotFoundException;
import com.jsp.lostAndFound.repository.ItemRepository;
import com.jsp.lostAndFound.repository.RecoveryRequestRepository;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class RecoveryRequestServiceImpl implements RecoveryRequestService {

    private final RecoveryRequestRepository recoveryRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public RecoveryRequestServiceImpl(
            RecoveryRequestRepository recoveryRequestRepository,
            ItemRepository itemRepository,
            UserRepository userRepository,
            NotificationService notificationService) {

        this.recoveryRequestRepository = recoveryRequestRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // =========================================================
    // CREATE RECOVERY REQUEST
    // =========================================================

    @Override
    public RecoveryRequestResponseDTO createRecoveryRequest(
            Long requesterId,
            RecoveryRequestRequestDTO requestDTO) {

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + requesterId
                        )
                );

        Item lostItem = itemRepository.findById(
                requestDTO.getLostItemId()
        ).orElseThrow(() ->
                new ItemNotFoundException(
                        "Lost item not found with id: "
                                + requestDTO.getLostItemId()
                )
        );

        Item foundItem = itemRepository.findById(
                requestDTO.getFoundItemId()
        ).orElseThrow(() ->
                new ItemNotFoundException(
                        "Found item not found with id: "
                                + requestDTO.getFoundItemId()
                )
        );

        validateItems(lostItem, foundItem);
        validateRequester(requester, lostItem);
        validateDuplicateRequest(lostItem.getId());

        LocalDateTime now = LocalDateTime.now();

        RecoveryRequest recoveryRequest = new RecoveryRequest();

        recoveryRequest.setLostItem(lostItem);
        recoveryRequest.setFoundItem(foundItem);
        recoveryRequest.setStatus(RecoveryRequestStatus.PENDING);
        recoveryRequest.setMessage(requestDTO.getMessage().trim());
        recoveryRequest.setCreatedAt(now);
        recoveryRequest.setUpdatedAt(now);

        RecoveryRequest savedRequest =
                recoveryRequestRepository.save(recoveryRequest);

        // Notify the finder
        Long finderId = foundItem.getReportedBy().getId();

        notificationService.createNotification(
                finderId,
                "Someone has submitted a recovery request for your found item.",
                NotificationType.RECOVERY_REQUEST_CREATED
        );

        return convertToResponseDTO(savedRequest);
    }

    // =========================================================
    // VALIDATE ITEMS
    // =========================================================

    private void validateItems(Item lostItem, Item foundItem) {

        if (lostItem.getItemType() != ItemType.LOST) {
            throw new IllegalArgumentException(
                    "The selected lost item must have type LOST"
            );
        }

        if (foundItem.getItemType() != ItemType.FOUND) {
            throw new IllegalArgumentException(
                    "The selected found item must have type FOUND"
            );
        }

        if (foundItem.getStatus() != ItemStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Recovery request can only be sent for an active found item"
            );
        }

        if (lostItem.getId().equals(foundItem.getId())) {
            throw new IllegalArgumentException(
                    "Lost item and found item cannot be the same"
            );
        }
    }

    // =========================================================
    // VALIDATE REQUESTER
    // =========================================================

    private void validateRequester(
            User requester,
            Item lostItem) {

        Long ownerId = lostItem.getReportedBy().getId();

        if (!requester.getId().equals(ownerId)) {
            throw new IllegalArgumentException(
                    "Only the owner of the lost item can create a recovery request"
            );
        }
    }

    // =========================================================
    // PREVENT DUPLICATE ACTIVE REQUEST
    // =========================================================

    private void validateDuplicateRequest(Long lostItemId) {

        List<RecoveryRequestStatus> activeStatuses = List.of(
                RecoveryRequestStatus.PENDING,
                RecoveryRequestStatus.ACCEPTED,
                RecoveryRequestStatus.RETURNED
        );

        boolean exists =
                recoveryRequestRepository
                        .existsByLostItemIdAndStatusIn(
                                lostItemId,
                                activeStatuses
                        );

        if (exists) {
            throw new IllegalArgumentException(
                    "An active recovery request already exists for this lost item"
            );
        }
    }

    // =========================================================
    // GET REQUEST BY ID
    // =========================================================

    @Override
    public RecoveryRequestResponseDTO getRecoveryRequestById(
            Long requestId,
            Long requesterId) {

        RecoveryRequest request = getRequest(requestId);

        validateParticipant(request, requesterId);

        return convertToResponseDTO(request);
    }

    // =========================================================
    // GET REQUESTS FOR LOST ITEM
    // =========================================================

    @Override
    public List<RecoveryRequestResponseDTO> getRequestsForLostItem(
            Long lostItemId,
            Long requesterId) {

        List<RecoveryRequest> requests =
                recoveryRequestRepository.findByLostItemId(lostItemId);

        if (requests.isEmpty()) {
            return List.of();
        }

        validateParticipant(requests.get(0), requesterId);

        return requests.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // GET REQUESTS FOR FOUND ITEM
    // =========================================================

    @Override
    public List<RecoveryRequestResponseDTO> getRequestsForFoundItem(
            Long foundItemId,
            Long requesterId) {

        List<RecoveryRequest> requests =
                recoveryRequestRepository.findByFoundItemId(foundItemId);

        if (requests.isEmpty()) {
            return List.of();
        }

        validateParticipant(requests.get(0), requesterId);

        return requests.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // ACCEPT REQUEST
    // Finder only
    // PENDING → ACCEPTED
    // Lost Item  → MATCHED
    // Found Item → CLAIMED
    // =========================================================

    @Override
    @Transactional
    public RecoveryRequestResponseDTO acceptRequest(
            Long requestId,
            Long finderId) {

        RecoveryRequest request = getRequest(requestId);

        validateFinder(request, finderId);

        if (request.getStatus() != RecoveryRequestStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only a PENDING request can be accepted"
            );
        }

        request.setStatus(RecoveryRequestStatus.ACCEPTED);
        request.setUpdatedAt(LocalDateTime.now());

        Item lostItem = request.getLostItem();
        Item foundItem = request.getFoundItem();

        lostItem.setStatus(ItemStatus.MATCHED);
        foundItem.setStatus(ItemStatus.CLAIMED);

        itemRepository.save(lostItem);
        itemRepository.save(foundItem);

        RecoveryRequest updatedRequest =
                recoveryRequestRepository.save(request);

        // Notify the owner
        Long ownerId = lostItem.getReportedBy().getId();

        notificationService.createNotification(
                ownerId,
                "The finder has accepted your recovery request.",
                NotificationType.RECOVERY_REQUEST_ACCEPTED
        );

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // REJECT REQUEST
    // Finder only
    // PENDING → REJECTED
    // =========================================================

    @Override
    @Transactional
    public RecoveryRequestResponseDTO rejectRequest(
            Long requestId,
            Long finderId) {

        RecoveryRequest request = getRequest(requestId);

        validateFinder(request, finderId);

        if (request.getStatus() != RecoveryRequestStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only a PENDING request can be rejected"
            );
        }

        request.setStatus(RecoveryRequestStatus.REJECTED);
        request.setUpdatedAt(LocalDateTime.now());

        RecoveryRequest updatedRequest =
                recoveryRequestRepository.save(request);

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // CANCEL REQUEST
    // Owner only
    // PENDING → CANCELLED
    // =========================================================

    @Override
    @Transactional
    public RecoveryRequestResponseDTO cancelRequest(
            Long requestId,
            Long ownerId) {

        RecoveryRequest request = getRequest(requestId);

        validateOwner(request, ownerId);

        if (request.getStatus() != RecoveryRequestStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only a PENDING request can be cancelled"
            );
        }

        request.setStatus(RecoveryRequestStatus.CANCELLED);
        request.setUpdatedAt(LocalDateTime.now());

        RecoveryRequest updatedRequest =
                recoveryRequestRepository.save(request);

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // MARK AS RETURNED
    // Finder only
    // ACCEPTED → RETURNED
    // =========================================================

    @Override
    @Transactional
    public RecoveryRequestResponseDTO markAsReturned(
            Long requestId,
            Long finderId) {

        RecoveryRequest request = getRequest(requestId);

        validateFinder(request, finderId);

        if (request.getStatus() != RecoveryRequestStatus.ACCEPTED) {
            throw new IllegalArgumentException(
                    "Only an ACCEPTED request can be marked as returned"
            );
        }

        request.setStatus(RecoveryRequestStatus.RETURNED);
        request.setUpdatedAt(LocalDateTime.now());

        RecoveryRequest updatedRequest =
                recoveryRequestRepository.save(request);

        Item lostItem = request.getLostItem();

        // Notify the owner
        Long ownerId = lostItem.getReportedBy().getId();

        notificationService.createNotification(
                ownerId,
                "The finder has marked your item as returned.",
                NotificationType.ITEM_RETURNED
        );

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // COMPLETE REQUEST
    // Owner only
    // RETURNED → COMPLETED
    // Lost Item  → RECOVERED
    // Found Item → CLOSED
    // =========================================================

    @Override
    @Transactional
    public RecoveryRequestResponseDTO completeRequest(
            Long requestId,
            Long ownerId) {

        RecoveryRequest request = getRequest(requestId);

        validateOwner(request, ownerId);

        if (request.getStatus() != RecoveryRequestStatus.RETURNED) {
            throw new IllegalArgumentException(
                    "Only a RETURNED request can be completed"
            );
        }

        request.setStatus(RecoveryRequestStatus.COMPLETED);
        request.setUpdatedAt(LocalDateTime.now());

        Item lostItem = request.getLostItem();
        Item foundItem = request.getFoundItem();

        lostItem.setStatus(ItemStatus.RECOVERED);
        foundItem.setStatus(ItemStatus.CLOSED);

        itemRepository.save(lostItem);
        itemRepository.save(foundItem);

        RecoveryRequest updatedRequest =
                recoveryRequestRepository.save(request);

        // Notify the finder
        Long finderId = foundItem.getReportedBy().getId();

        notificationService.createNotification(
                finderId,
                "The owner has confirmed that the recovery is completed.",
                NotificationType.RECOVERY_COMPLETED
        );

        return convertToResponseDTO(updatedRequest);
    }

    // =========================================================
    // GET REQUEST
    // =========================================================

    private RecoveryRequest getRequest(Long requestId) {

        return recoveryRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Recovery request not found with id: "
                                        + requestId
                        )
                );
    }

    // =========================================================
    // VALIDATE PARTICIPANT
    // Owner OR Finder can view request
    // =========================================================

    private void validateParticipant(
            RecoveryRequest request,
            Long requesterId) {

        Long ownerId =
                request.getLostItem().getReportedBy().getId();

        Long finderId =
                request.getFoundItem().getReportedBy().getId();

        if (!requesterId.equals(ownerId)
                && !requesterId.equals(finderId)) {

            throw new IllegalArgumentException(
                    "You are not authorized to view this recovery request"
            );
        }
    }

    // =========================================================
    // VALIDATE FINDER
    // =========================================================

    private void validateFinder(
            RecoveryRequest request,
            Long finderId) {

        Long actualFinderId =
                request.getFoundItem().getReportedBy().getId();

        if (!finderId.equals(actualFinderId)) {
            throw new IllegalArgumentException(
                    "Only the finder of the item can perform this action"
            );
        }
    }

    // =========================================================
    // VALIDATE OWNER
    // =========================================================

    private void validateOwner(
            RecoveryRequest request,
            Long ownerId) {

        Long actualOwnerId =
                request.getLostItem().getReportedBy().getId();

        if (!ownerId.equals(actualOwnerId)) {
            throw new IllegalArgumentException(
                    "Only the owner of the lost item can perform this action"
            );
        }
    }

    // =========================================================
    // CONVERT ENTITY → DTO
    // =========================================================

    private RecoveryRequestResponseDTO convertToResponseDTO(
            RecoveryRequest request) {

        Item lostItem = request.getLostItem();
        Item foundItem = request.getFoundItem();

        User owner = lostItem.getReportedBy();
        User finder = foundItem.getReportedBy();

        return new RecoveryRequestResponseDTO(
                request.getId(),
                lostItem.getId(),
                lostItem.getTitle(),
                foundItem.getId(),
                foundItem.getTitle(),
                owner.getId(),
                owner.getName(),
                finder.getId(),
                finder.getName(),
                request.getStatus(),
                request.getMessage(),
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}