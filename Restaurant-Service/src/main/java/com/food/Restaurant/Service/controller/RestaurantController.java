package com.food.Restaurant.Service.controller;

import com.food.Restaurant.Service.model.MenuItem;
import com.food.Restaurant.Service.model.RestaurantDto;
import com.food.Restaurant.Service.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    
    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService service;

    @PostMapping
    public RestaurantDto create(@Valid @RequestBody RestaurantDto dto) {
        log.info("Received request to create new Restaurant: {}", dto.getName());
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public RestaurantDto get(@PathVariable Long id) {
        log.info("Fetching details for Restaurant ID: {}", id);
        return service.get(id);
    }

    @PutMapping("/{id}")
    public RestaurantDto update(@PathVariable Long id,
                                @RequestBody RestaurantDto dto) {
        log.info("Received request to update Restaurant ID: {}", id);
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.warn("Received request to delete Restaurant ID: {}", id);
        service.delete(id);
    }

    @PostMapping("/{id}/menu")
    public MenuItem addMenu(@PathVariable Long id,
                                                              @RequestBody MenuItem item) {
        return service.addMenuItem(id, item);
    }

    @DeleteMapping("/{id}/menu/{itemId}")
    public void removeMenu(@PathVariable Long id, @PathVariable Long itemId) {
        log.info("Removing Menu Item ID: {} from Restaurant ID: {}", itemId, id);
        service.removeMenuItem(itemId);
    }
}