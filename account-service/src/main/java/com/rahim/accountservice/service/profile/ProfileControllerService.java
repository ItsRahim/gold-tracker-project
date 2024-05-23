package com.rahim.accountservice.service.profile;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 23/05/2024
 */
@Service
@RequiredArgsConstructor
public class ProfileControllerService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileControllerService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    public ResponseEntity<Object> getProfileByUsername(String username) {
        try {
            Profile profile = profileRepositoryHandler.getProfileByUsername(username);
            if (profile.getId() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile with username " + username + " not found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(profile);
        } catch (Exception e) {
            LOG.error("An error occurred retrieving profile by username: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Error finding user profile");
        }
    }

    public ResponseEntity<Object> getAllProfiles() {
        try {
            List<Profile> profiles = profileRepositoryHandler.getAllProfiles();
            return ResponseEntity.status(HttpStatus.OK).body(profiles);
        } catch (Exception e) {
            LOG.error("Error retrieving all profiles: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Error retreiving all profiles");
        }
    }
}
