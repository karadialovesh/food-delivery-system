package com.food.Restaurant.Service.service;

import com.food.Restaurant.Service.model.MenuItem;
import com.food.Restaurant.Service.model.Restaurant;
import com.food.Restaurant.Service.model.RestaurantDto;
import com.food.Restaurant.Service.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    
    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository repository;
    private final com.food.Restaurant.Service.repository.MenuItemRepository menuItemRepository;

    public RestaurantDto create(RestaurantDto dto) {
        Restaurant restaurant = mapToEntity(dto);
        Restaurant saved = repository.save(restaurant);
        log.info("New Restaurant created with ID: {} and Name: {}", saved.getId(), saved.getName());
        return mapToDto(saved);
    }

    public MenuItem addMenuItem(Long restaurantId, MenuItem item) {
        find(restaurantId); // Ensure restaurant exists
        item.setRestaurantId(restaurantId);
        return menuItemRepository.save(item);
    }

    public void removeMenuItem(Long itemId) {
        menuItemRepository.deleteById(itemId);
    }

    public RestaurantDto get(Long id) {
        return mapToDto(find(id));
    }

    public RestaurantDto update(Long id, RestaurantDto dto) {
        Restaurant restaurant = find(id);
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setLatitude(dto.getLatitude());
        restaurant.setLongitude(dto.getLongitude());
        return mapToDto(repository.save(restaurant));
    }

    public void delete(Long id) {
        log.warn("Deleting Restaurant with ID: {}", id);
        repository.deleteById(id);
    }

    private Restaurant find(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    private Restaurant mapToEntity(RestaurantDto dto) {
        return Restaurant.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();
    }

    private RestaurantDto mapToDto(Restaurant r) {
        return new RestaurantDto(
                r.getId(),
                r.getName(),
                r.getAddress(),
                r.getLatitude(),
                r.getLongitude()
        );
    }
}