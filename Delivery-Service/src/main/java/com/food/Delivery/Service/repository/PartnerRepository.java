package com.food.Delivery.Service.repository;

import com.food.Delivery.Service.model.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository extends JpaRepository<DeliveryPartner,Long> {

    List<DeliveryPartner> findByAvailableTrue();
}