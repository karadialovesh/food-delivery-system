package com.food.Order.Service.service;

import com.food.Order.Service.kafka.OrderProducer;
import com.food.Order.Service.model.Order;
import com.food.Order.Service.model.OrderItemRequest;
import com.food.Order.Service.model.OrderRequest;
import com.food.Order.Service.model.OrderStatus;
import com.food.Order.Service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;
    private final OrderProducer producer;

    public Order create(OrderRequest request) {
        double totalPrice = 0;
        for (OrderItemRequest item : request.getItems()) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .restaurantId(request.getRestaurantId())
                .deliveryLat(request.getDeliveryLat())
                .deliveryLon(request.getDeliveryLon())
                .deliveryAddress(request.getDeliveryAddress())
                .totalPrice(totalPrice)
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        if (request.getItems() != null) {
            java.util.List<com.food.Order.Service.model.OrderItem> items = request.getItems().stream()
                    .map(itemReq -> {
                        com.food.Order.Service.model.OrderItem item = new com.food.Order.Service.model.OrderItem();
                        item.setMenuItemId(itemReq.getMenuItemId());
                        item.setQuantity(itemReq.getQuantity());
                        item.setPrice(itemReq.getPrice());
                        item.setOrder(order);
                        return item;
                    }).collect(java.util.stream.Collectors.toList());
            order.setItems(items);

            // Calculate Total Price
            double calculatedTotal = items.stream()
                    .mapToDouble(i -> i.getPrice() * i.getQuantity())
                    .sum();
            order.setTotalPrice(calculatedTotal);
        }

        Order saved = repository.save(order);

        log.info("Order created successfully with ID: {}", saved.getId());
        producer.sendCreated(saved);

        return saved;
    }

    public Order updateStatus(Long id, OrderStatus status) {
        Order order = find(id);
        order.setStatus(status);
        return repository.save(order);
    }

    public void cancel(Long id) {
        Order order = find(id);
        order.setStatus(OrderStatus.CANCELLED);
        repository.save(order);
        log.warn("Order with ID: {} has been cancelled", id);
        producer.sendCancelled(order);
    }

    public Order get(Long id) {
        return find(id);
    }

    private Order find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}