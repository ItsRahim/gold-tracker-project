package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.profile.IProfileQueryService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for querying Profiles table
 *
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileQueryService implements IProfileQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileQueryService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    public boolean existsByUsername(String username) {
        return profileRepositoryHandler.existsByUsername(username);
    }

    @Override
    public Map<String, Object> getProfileDetails(int accountId) {
        try {
            Map<String, Object> userProfileDetails = profileRepositoryHandler.getProfileDetails(accountId);

            if (userProfileDetails != null) {
                LOG.debug("Account profile details retrieved successfully for user ID {}", accountId);
                return userProfileDetails;
            } else {
                LOG.info("No user profile details found for user ID {}", accountId);
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            LOG.error("Error retrieving user profile details for user ID {}: {}", accountId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while retrieving user profile details", e);
        }
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        try {
            Optional<Profile> profileOptional = profileRepositoryHandler.getProfileByUsername(username);

            if (profileOptional.isPresent()) {
                LOG.info("Profile found for username: {}", username);
            } else {
                LOG.info("Profile not found for username: {}", username);
            }

            return profileOptional;
        } catch (Exception e) {
            LOG.error("Error fetching user profile for username {}: {}", username, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = profileRepositoryHandler.getAllProfiles();

        if(!profiles.isEmpty()) {
            LOG.info("Found {} users in the database", profiles.size());
        } else {
            LOG.info("No users found in the database");
        }
        return profiles;
    }
}
