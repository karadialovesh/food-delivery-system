package com.food.Order.Service.model;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long menuItemId;
    private Integer quantity;
    private Double price;
}
