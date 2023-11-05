package com.rahim.userservice.controller;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.service.IUserProfileService;
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
@RequestMapping("/api/v1/gold/user-service/profile")
public class UserProfileController {
    private final IUserProfileService userProfileService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping()
    public ResponseEntity<List<UserProfile>> findAllProfiles() {
        List<UserProfile> userProfiles = userProfileService.getAllProfiles();
        return ResponseEntity.ok(userProfiles);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<?> findProfileById(@PathVariable int profileId) {
        try {
            Optional<UserProfile> profileOptional = userProfileService.getProfileById(profileId);

            if (profileOptional.isPresent()) {
                return ResponseEntity.ok(profileOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found for ID: " + profileId);
            }
        } catch (Exception e) {
            log.error("Error finding user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user profile");
        }
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable int profileId, @RequestBody Map<String, String> updatedData) {
        try {
            userProfileService.updateUserProfile(profileId, updatedData);
            return ResponseEntity.ok("User Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findProfileByUsername(@PathVariable String username) {
        try {
            Optional<UserProfile> profileOptional = userProfileService.getProfileByUsername(username);

            if (profileOptional.isPresent()) {
                return ResponseEntity.ok(profileOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found with username: " + username);
            }
        } catch (Exception e) {
            log.error("Error finding user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user profile");

        }
    }
}