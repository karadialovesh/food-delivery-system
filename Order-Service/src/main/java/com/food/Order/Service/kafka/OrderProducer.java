package com.food.Order.Service.kafka;

import com.food.Order.Service.model.Order;
import com.food.common.event.OrderCancelledEvent;
import com.food.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderProducer {
    
    private static final Logger log = LoggerFactory.getLogger(OrderProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendCreated(Order order) {
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .restaurantId(order.getRestaurantId())
                .totalPrice(order.getTotalPrice())
                .deliveryLat(order.getDeliveryLat())
                .deliveryLon(order.getDeliveryLon())
                .deliveryAddress(order.getDeliveryAddress())
                .build();
        log.info("Publishing OrderCreatedEvent for Order ID: {}", order.getId());
        kafkaTemplate.send("order.created", event);
    }

    public void sendCancelled(Order order) {
        OrderCancelledEvent event = OrderCancelledEvent.builder()
                .orderId(order.getId())
                .reason("Cancelled by user")
                .build();
        log.warn("Publishing OrderCancelledEvent for Order ID: {}", order.getId());
        kafkaTemplate.send("order.cancelled", event);
    }
}