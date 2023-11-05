package com.rahim.userservice.controller;

import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.IUserProfileService;
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
@RequestMapping("/api/v1/gold/user-service")
public class UserController {
    private final IUserService userService;
    private final IUserProfileService userProfileService;
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

    @GetMapping("/profile")
    public ResponseEntity<List<UserProfile>> findAllProfiles() {
        List<UserProfile> userProfiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(userProfiles);
    }

    @GetMapping("/profile/{profileId}")
    public ResponseEntity<?> findProfileById(@PathVariable int profileId) {
        try {
            Optional<UserProfile> userOptional = userProfileService.getProfileById(profileId);

            if (userOptional.isPresent()) {
                return ResponseEntity.ok(userOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found for ID: " + profileId);
            }
        } catch (Exception e) {
            log.error("Error finding user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user profile");
        }
    }

    @PutMapping("/profile/{profileId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int profileId, @RequestBody UserProfile userProfile) {
        try {
            Optional<UserProfile> existingProfile = userProfileService.getProfileById(profileId);

            if (existingProfile.isPresent()) {
                userProfile.setId(profileId);
                userProfileService.updateUserProfile(userProfile);
                return ResponseEntity.ok("User Profile updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
            }
        } catch (Exception e) {
            log.error("Error updating user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user profile");
        }
    }
}
