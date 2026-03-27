package com.food.Notification.Service.controller;

import com.food.Notification.Service.model.Notification;
import com.food.Notification.Service.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationRepository repository;

    public NotificationController(NotificationRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(
            @PathVariable Long userId) {
        log.info("Fetching notifications for User ID: {}", userId);
        return repository.findByUserId(userId);
    }
}