package com.food.Notification.Service.kafka;

import com.food.common.event.OrderCreatedEvent;
import com.food.common.event.OrderPickedUpEvent;
import com.food.Notification.Service.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final NotificationService notificationService;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(
            topics = "order.created",
            groupId = "notification-group")
    public void consume(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for Order ID: {}", event.getOrderId());

        String message =
                "Your order #" + event.getOrderId() +
                " has been placed successfully.";

        notificationService.createNotification(
                event.getUserId(),
                message
        );
    }

    @KafkaListener(
            topics = "order.picked-up",
            groupId = "notification-group")
    public void consumePickup(OrderPickedUpEvent event) {
        log.info("Received OrderPickedUpEvent for Order ID: {}", event.getOrderId());
    

        String message =
                "Your order #" + event.getOrderId() +
                " has been picked up by the delivery agent and is on the way!";

        notificationService.createNotification(
                event.getUserId(),
                message
        );
    }
}