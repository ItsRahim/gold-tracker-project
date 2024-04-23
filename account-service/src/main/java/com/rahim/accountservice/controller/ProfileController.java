package com.rahim.accountservice.controller;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rahim.accountservice.constant.ProfileControllerEndpoint.*;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class ProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IProfileUpdateService profileUpdateService;

    @GetMapping()
    public ResponseEntity<List<Profile>> getAllProfiles() {
        List<Profile> profiles = profileRepositoryHandler.getAllProfiles();
        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @PutMapping(PROFILE_ID)
    public ResponseEntity<String> updateUserProfile(@PathVariable int profileId, @RequestBody Map<String, String> updatedData) {
        try {
            profileUpdateService.updateProfile(profileId, updatedData);
            return ResponseEntity.status(HttpStatus.OK).body("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account profile not found");
        }
    }

    @GetMapping(USERNAME)
    public ResponseEntity<Object> findProfileByUsername(@PathVariable String username) {
        try {
            Optional<Profile> profileOptional = profileRepositoryHandler.getProfileByUsername(username);

            if (profileOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(profileOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found with username: " + username);
            }
        } catch (Exception e) {
            LOG.error("Error finding user profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finding user profile");
        }
    }
}