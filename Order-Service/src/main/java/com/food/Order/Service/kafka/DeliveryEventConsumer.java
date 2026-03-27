package com.food.Order.Service.kafka;

import com.food.Order.Service.model.Order;
import com.food.Order.Service.model.OrderStatus;
import com.food.Order.Service.repository.OrderRepository;
import com.food.common.event.OrderPickedUpEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(DeliveryEventConsumer.class);

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order.picked-up", groupId = "order-group")
    public void consumeOrderPickedUp(OrderPickedUpEvent event) {
        log.info("Received OrderPickedUpEvent for Order ID: {}", event.getOrderId());
        Order order = orderRepository.findById(event.getOrderId()).orElse(null);
        if (order != null) {
            order.setStatus(OrderStatus.PICKED);
            orderRepository.save(order);
            log.info("Updated Order ID {} to status PICKED", event.getOrderId());
        }
    }
}
