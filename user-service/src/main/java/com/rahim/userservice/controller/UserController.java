package com.rahim.userservice.controller;

import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/user-service/user")
public class UserController {
    private final IUserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        try {
            userService.createUserAndProfile(userRequest);
            log.info("Successfully Created User: {}", userRequest.getUserProfile().getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("User and User Profile created successfully");
        } catch (Exception e) {
            log.error("Error creating User and User Profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating User and User Profile");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findUserById(@PathVariable int userId) {
        try {
            Optional<User> userOptional = userService.findUserById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                log.info("User found with ID: {}", userId);
                return ResponseEntity.ok(user);
            } else {
                log.info("User not found with ID: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            log.error("Error finding user with ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user");
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody Map<String, String> updatedData) {
        try {
            userService.updateUser(userId, updatedData);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @GetMapping()
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        try {
            boolean deleted = userService.deleteUserRequest(userId);

            if (deleted) {
                return ResponseEntity.ok("Successfully Requested to Delete User with ID: " + userId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID: " + userId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user: " + e.getMessage());
        }
    }
}