package com.food.Delivery.Service.service;

import com.food.Delivery.Service.model.Delivery;
import com.food.Delivery.Service.model.DeliveryStatus;
import com.food.Delivery.Service.repository.DeliveryRepository;
import com.food.Delivery.Service.kafka.DeliveryProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {

    private static final Logger log = LoggerFactory.getLogger(DeliveryScheduler.class);

    private final DeliveryRepository repository;
    private final DeliveryService deliveryService;
    private final DeliveryProducer producer;

    @Scheduled(fixedRate = 60000) // Every 1 minute
    public void retry() {
        log.info("Running delivery retry/cleanup scheduler");
        List<Delivery> searchingDeliveries = repository.findByStatus(DeliveryStatus.SEARCHING);

        for (Delivery delivery : searchingDeliveries) {
            LocalDateTime now = LocalDateTime.now();
            
            // if > 1.5 hr → cancel order
            if (delivery.getCreatedAt().isBefore(now.minusMinutes(90))) {
                log.warn("Cancelling order {} due to no agent found within 90 mins", delivery.getOrderId());
                delivery.setStatus(DeliveryStatus.CANCELLED);
                repository.save(delivery);
                producer.cancelOrder(delivery.getOrderId());
                continue;
            }

            // if > 10 min → search in 10kb
            if (delivery.getCreatedAt().isBefore(now.minusMinutes(10))) {
                log.info("Retrying order {} with 10km radius", delivery.getOrderId());
                deliveryService.retryAssignment(delivery, 10);
            } else {
                // otherwise retry with 5km
                log.info("Retrying order {} with 5km radius", delivery.getOrderId());
                deliveryService.retryAssignment(delivery, 5);
            }
        }
    }
}