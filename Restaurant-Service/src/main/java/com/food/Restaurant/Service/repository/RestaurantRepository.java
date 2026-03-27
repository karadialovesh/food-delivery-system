package com.food.Restaurant.Service.repository;

import com.food.Restaurant.Service.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}