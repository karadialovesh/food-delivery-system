package com.food.Delivery.Service.controller;

import com.food.Delivery.Service.model.Delivery;
import com.food.Delivery.Service.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    
    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    private final DeliveryRepository deliveryRepository;
    private final com.food.Delivery.Service.service.DeliveryService deliveryService;

    public DeliveryController(DeliveryRepository deliveryRepository, com.food.Delivery.Service.service.DeliveryService deliveryService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryService = deliveryService;
    }

    @GetMapping("/{orderId}")
    public Delivery getDelivery(@PathVariable Long orderId){
        log.info("Fetching delivery details for Order ID: {}", orderId);
        return deliveryRepository.findByOrderId(orderId);
    }

    @org.springframework.web.bind.annotation.PostMapping("/partner")
    public com.food.Delivery.Service.model.DeliveryPartner registerPartner(
            @org.springframework.web.bind.annotation.RequestBody com.food.Delivery.Service.model.DeliveryPartner partner) {
        log.info("Registering new delivery partner: {}", partner.getName());
        return deliveryService.registerPartner(partner);
    }

    @org.springframework.web.bind.annotation.PostMapping("/{orderId}/self-assign")
    public Delivery selfAssign(@PathVariable Long orderId, @org.springframework.web.bind.annotation.RequestParam Long partnerId) {
        log.info("Self-assigning Order ID: {} to Partner ID: {}", orderId, partnerId);
        return deliveryService.selfAssign(orderId, partnerId);
    }

    @org.springframework.web.bind.annotation.PostMapping("/{orderId}/pickup")
    public Delivery pickupOrder(@PathVariable Long orderId) {
        log.info("Partner picking up Order ID: {}", orderId);
        return deliveryService.pickupOrder(orderId);
    }
}