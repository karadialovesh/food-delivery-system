package com.food.Delivery.Service.kafka;

import com.food.Delivery.Service.model.Delivery;
import com.food.common.event.OrderPickedUpEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryProducer {
    
    private static final Logger log = LoggerFactory.getLogger(DeliveryProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void assigned(Delivery delivery) {
        log.info("Publishing DeliveryAssignedEvent for Order ID: {} and Agent ID: {}", delivery.getOrderId(), delivery.getAgentId());
        kafkaTemplate.send("delivery.assigned", delivery);
    }

    public void sendPickedUp(Long orderId, Long userId) {
        log.info("Publishing OrderPickedUpEvent for Order ID: {}", orderId);
        OrderPickedUpEvent event = new OrderPickedUpEvent(orderId, userId);
        kafkaTemplate.send("order.picked-up", event);
    }

    public void cancelOrder(Long orderId) {
        kafkaTemplate.send("order.cancelled", orderId);
    }
}