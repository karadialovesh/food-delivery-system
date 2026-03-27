package com.food.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private Double totalPrice;
    private double deliveryLat;
    private double deliveryLon;
    private String deliveryAddress;
}
