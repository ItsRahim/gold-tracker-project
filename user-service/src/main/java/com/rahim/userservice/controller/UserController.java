package com.rahim.userservice.controller;

import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final IUserService userService;
    private final IUserProfileService userProfileService;

    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        User user = userRequest.getUser();
        UserProfile userProfile = userRequest.getUserProfile();

        try {
            userService.save(user);

            userProfile.setUser(user);
            userProfileService.save(userProfile);

            log.info("Successfully Created User: {}", userProfile.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body("User and User Profile created successfully");

        } catch (Exception e) {
            log.error("Error creating User and User Profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating User and User Profile");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAccount(@PathVariable int id) {
        try {
            Optional<User> userOptional = userService.findById(id);

            if (userOptional.isPresent()) {
                log.info("User found with ID: {}", id);
                return ResponseEntity.ok(userOptional.get());
            } else {
                log.info("User not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error finding user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding User");
        }
    }


    @GetMapping()
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.findAllUsers();
        return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.noContent().build();
    }


    @GetMapping("/profile")
    public ResponseEntity<List<UserProfile>> findAllProfiles() {
        List<UserProfile> userProfiles = userProfileService.findAllUserProfiles();
        return !userProfiles.isEmpty() ? ResponseEntity.ok(userProfiles) : ResponseEntity.noContent().build();
    }


    @GetMapping("/profile/{id}")
    public ResponseEntity<?> findProfile(@PathVariable int id) {
        try {
            Optional<UserProfile> userOptional = userProfileService.findById(id);

            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found for ID: " + id);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user profile: " + e.getMessage());
        }
    }


}
