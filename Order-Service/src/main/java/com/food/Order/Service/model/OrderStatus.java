package com.food.Order.Service.model;

public enum OrderStatus {
    CREATED,
    ACCEPTED,
    PREPARING,
    PREPARED,
    WAITING_DELIVERY,
    PICKED,
    DELIVERED,
    CANCELLED
}
