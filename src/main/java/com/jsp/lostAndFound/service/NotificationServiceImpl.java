package com.jsp.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.NotificationResponseDTO;
import com.jsp.lostAndFound.entity.Notification;
import com.jsp.lostAndFound.entity.NotificationType;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.exception.UserNotFoundException;
import com.jsp.lostAndFound.repository.NotificationRepository;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    @Override
    public NotificationResponseDTO createNotification(
            Long userId,
            String message,
            NotificationType type) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Notification message cannot be empty"
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Notification type is required"
            );
        }

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setMessage(message.trim());
        notification.setType(type);

        // New notifications are unread
        notification.setRead(false);

        notification.setCreatedAt(LocalDateTime.now());

        Notification savedNotification =
                notificationRepository.save(notification);

        return convertToResponseDTO(savedNotification);
    }

    // =========================================================
    // GET ALL NOTIFICATIONS FOR USER
    // =========================================================

    @Override
    public List<NotificationResponseDTO> getNotificationsForUser(
            Long userId) {

        validateUser(userId);

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // GET UNREAD NOTIFICATIONS
    // =========================================================

    @Override
    public List<NotificationResponseDTO> getUnreadNotifications(
            Long userId) {

        validateUser(userId);

        return notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // =========================================================
    // GET UNREAD COUNT
    // =========================================================

    @Override
    public long getUnreadCount(Long userId) {

        validateUser(userId);

        return notificationRepository
                .countByUserIdAndIsReadFalse(userId);
    }

    // =========================================================
    // MARK NOTIFICATION AS READ
    // =========================================================

    @Override
    public NotificationResponseDTO markAsRead(
            Long notificationId,
            Long userId) {

        validateUser(userId);

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification not found with id: "
                                                + notificationId
                                )
                        );

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "You are not authorized to update this notification"
            );
        }

        notification.setRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return convertToResponseDTO(updatedNotification);
    }

    // =========================================================
    // VALIDATE USER
    // =========================================================

    private void validateUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(
                    "User not found with id: " + userId
            );
        }
    }

    // =========================================================
    // ENTITY → DTO
    // =========================================================

    private NotificationResponseDTO convertToResponseDTO(
            Notification notification) {

        return new NotificationResponseDTO(
                notification.getId(),
                notification.getUser().getId(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}