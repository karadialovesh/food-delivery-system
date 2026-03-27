package com.food.Order.Service.model;

import lombok.Data;

@Data
public class OrderRequest {
    private Long userId;
    private Long restaurantId;

    private double deliveryLat;
    private double deliveryLon;
    private String deliveryAddress;

    private java.util.List<OrderItemRequest> items;
}