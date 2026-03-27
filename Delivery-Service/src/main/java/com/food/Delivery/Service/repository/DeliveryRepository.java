package com.food.Delivery.Service.repository;

import com.food.Delivery.Service.model.Delivery;
import com.food.Delivery.Service.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Delivery findByOrderId(Long orderId);

    List<Delivery> findByStatus(DeliveryStatus status);
}