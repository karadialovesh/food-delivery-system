package com.food.Delivery.Service.service;

import com.food.Delivery.Service.kafka.DeliveryProducer;
import com.food.Delivery.Service.model.Delivery;
import com.food.Delivery.Service.model.DeliveryPartner;
import com.food.Delivery.Service.model.DeliveryStatus;
import com.food.Delivery.Service.redis.AgentLocationService;
import com.food.Delivery.Service.repository.DeliveryPartnerRepository;
import com.food.Delivery.Service.repository.DeliveryRepository;
import com.food.common.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    
    private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);

    private final AgentLocationService agentService;
    private final DeliveryRepository repository;
    private final DeliveryPartnerRepository partnerRepository;
    private final DeliveryProducer producer;

    public void handleNewOrder(OrderCreatedEvent event) {

        double lat = event.getDeliveryLat();
        double lon = event.getDeliveryLon();

        // Step 1: try 5 km
        var agents = agentService.findNearby(lat, lon, 5);

        if (agents != null && !agents.getContent().isEmpty()) {
            GeoResult<RedisGeoCommands.GeoLocation<Object>> firstAgentResult = agents.getContent().get(0);
            log.info("Found nearby agents for Order ID: {}. Assigning to agent: {}", event.getOrderId(), firstAgentResult.getContent().getName());
            assign(event, firstAgentResult.getContent().getName());
        } else {
            log.warn("No nearby agents found for Order ID: {}. Saving as SEARCHING.", event.getOrderId());
            // Save as SEARCHING for scheduler to pick up
            Delivery delivery = Delivery.builder()
                    .orderId(event.getOrderId())
                    .userId(event.getUserId())
                    .status(DeliveryStatus.SEARCHING)
                    .deliveryLat(lat)
                    .deliveryLon(lon)
                    .createdAt(LocalDateTime.now())
                    .build();
            repository.save(delivery);
        }
    }

    private void assign(OrderCreatedEvent event, Object agentId) {
        log.info("Assigning Order ID: {} to Agent ID: {}", event.getOrderId(), agentId);
        Delivery delivery = Delivery.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .agentId(Long.valueOf(agentId.toString()))
                .status(DeliveryStatus.ASSIGNED)
                .deliveryLat(event.getDeliveryLat())
                .deliveryLon(event.getDeliveryLon())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(delivery);

        producer.assigned(delivery);
    }

    public void retryAssignment(Delivery delivery, double radius) {
        var agents = agentService.findNearby(delivery.getDeliveryLat(), delivery.getDeliveryLon(), radius);

        if (agents != null && !agents.getContent().isEmpty()) {
            GeoResult<RedisGeoCommands.GeoLocation<Object>> firstAgentResult = agents.getContent().get(0);
            Object agentId = firstAgentResult.getContent().getName();

            delivery.setAgentId(Long.valueOf(agentId.toString()));
            delivery.setStatus(DeliveryStatus.ASSIGNED);
            repository.save(delivery);
            log.info("Successfully reassigned Order ID: {} to Agent ID: {} on retry with radius: {}", delivery.getOrderId(), agentId, radius);
            producer.assigned(delivery);
        } else {
            log.warn("Retry failed for Order ID: {} with radius: {}", delivery.getOrderId(), radius);
        }
    }

    public DeliveryPartner registerPartner(DeliveryPartner partner) {
        return partnerRepository.save(partner);
    }

    public Delivery selfAssign(Long orderId, Long partnerId) {
        Delivery delivery = repository.findByOrderId(orderId);
        if (delivery == null) {
            delivery = Delivery.builder()
                    .orderId(orderId)
                    .agentId(partnerId)
                    .status(DeliveryStatus.ASSIGNED)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            delivery.setAgentId(partnerId);
            delivery.setStatus(DeliveryStatus.ASSIGNED);
        }
        return repository.save(delivery);
    }

    public Delivery pickupOrder(Long orderId) {
        Delivery delivery = repository.findByOrderId(orderId);
        if (delivery != null) {
            delivery.setStatus(DeliveryStatus.PICKED_UP);
            Delivery savedDelivery = repository.save(delivery);
            producer.sendPickedUp(savedDelivery.getOrderId(), savedDelivery.getUserId());
            log.info("Order ID: {} officially picked up by courier", orderId);
            return savedDelivery;
        }
        log.error("Failed to pickup Order ID: {}. Delivery record not found.", orderId);
        return null;
    }
}
