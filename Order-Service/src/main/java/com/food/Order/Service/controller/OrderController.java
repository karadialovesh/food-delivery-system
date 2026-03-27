package com.food.Order.Service.controller;

import com.food.Order.Service.model.Order;
import com.food.Order.Service.model.OrderRequest;
import com.food.Order.Service.model.OrderStatus;
import com.food.Order.Service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody OrderRequest request) {
        log.info("Received request to create order for User ID: {}", request.getUserId());
        return service.create(request);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id,
                             @RequestParam OrderStatus status) {
        log.info("Received request to update Order ID: {} to status: {}", id, status);
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        service.cancel(id);
    }
}