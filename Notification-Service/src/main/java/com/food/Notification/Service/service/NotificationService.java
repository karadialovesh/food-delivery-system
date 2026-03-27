package com.food.Notification.Service.service;

import com.food.Notification.Service.model.Notification;
import com.food.Notification.Service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createNotification(Long userId, String message) {
        log.info("Creating notification for User ID {}: {}", userId, message);
        Notification notification = new Notification();

        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());

        repository.save(notification);
    }

}