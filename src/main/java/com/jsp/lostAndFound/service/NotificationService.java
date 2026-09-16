package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.NotificationResponseDTO;
import com.jsp.lostAndFound.entity.NotificationType;

public interface NotificationService {

    NotificationResponseDTO createNotification(
            Long userId,
            String message,
            NotificationType type);

    List<NotificationResponseDTO> getNotificationsForUser(
            Long userId);

    List<NotificationResponseDTO> getUnreadNotifications(
            Long userId);

    long getUnreadCount(Long userId);

    NotificationResponseDTO markAsRead(
            Long notificationId,
            Long userId);
}