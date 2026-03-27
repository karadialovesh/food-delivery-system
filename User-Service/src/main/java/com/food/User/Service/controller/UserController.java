package com.food.User.Service.controller;

import com.food.User.Service.model.UserDto;
import com.food.User.Service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        log.info("Request to fetch User profile for ID: {}", id);
        return service.get(id);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id,
                          @RequestBody UserDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}