package com.food.Order.Service.repository;

import com.food.Order.Service.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository
        extends JpaRepository<OrderItem,Long> {

}