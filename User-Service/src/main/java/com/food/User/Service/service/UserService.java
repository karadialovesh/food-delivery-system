package com.food.User.Service.service;

import com.food.User.Service.model.User;
import com.food.User.Service.model.UserDto;
import com.food.User.Service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserDto create(UserDto dto) {

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
        User saved = userRepository.save(user);
        log.info("New User created with ID: {} and Email: {}", saved.getId(), saved.getEmail());
        return mapToDto(saved);
    }

    public UserDto get(Long id) {
        return mapToDto(findUser(id));
    }

    public UserDto update(Long id, UserDto dto) {

        User user = findUser(id);

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());

        return mapToDto(userRepository.save(user));
    }

    public void delete(Long id) {
        log.warn("Deleting User with ID: {}", id);
        userRepository.deleteById(id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
        );
    }
}