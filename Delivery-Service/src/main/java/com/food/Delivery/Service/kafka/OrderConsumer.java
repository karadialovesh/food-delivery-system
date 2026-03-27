package com.food.Delivery.Service.kafka;

import com.food.common.event.OrderCreatedEvent;
import com.food.Delivery.Service.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConsumer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    private final DeliveryService deliveryService;

    @KafkaListener(topics = "order.created", groupId = "delivery-group")
    public void consume(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for Order ID: {}", event.getOrderId());
        deliveryService.handleNewOrder(event);
    }
}