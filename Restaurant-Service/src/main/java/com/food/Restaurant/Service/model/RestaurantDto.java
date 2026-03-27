package com.food.Restaurant.Service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDto {

    private Long id;

    @NotBlank
    private String name;

    private String address;

    private double latitude;
    private double longitude;
}