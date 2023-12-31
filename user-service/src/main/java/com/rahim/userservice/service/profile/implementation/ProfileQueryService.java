package com.rahim.userservice.service.profile.implementation;

import com.rahim.userservice.model.Profile;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                LOG.info("Account profile details retrieved successfully for user ID {}: {}", accountId, userProfileDetails);
                return userProfileDetails;
            } else {
                LOG.info("No user profile details found for user ID {}", accountId);
                return null;
            }
        } catch (Exception e) {
            LOG.error("Error retrieving user profile details for user ID {}: {}", accountId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while retrieving user profile details", e);
        }
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        try {
            Optional<Profile> profileOptional = profileRepositoryHandler.findByUsername(username);

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
        List<Profile> profiles = profileRepositoryHandler.findAll();

        if(!profiles.isEmpty()) {
            LOG.info("Found {} users in the database", profiles.size());
        } else {
            LOG.info("No users found in the database");
        }
        return profiles;
    }
}
