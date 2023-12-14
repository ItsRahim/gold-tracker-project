package com.rahim.userservice.service.implementation;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final IUserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserProfileService.class);

    @Override
    public void createUserProfile(UserProfile userProfile) {
        try {
            userProfileRepository.save(userProfile);
            LOG.info("User profile created successfully. ID: {}", userProfile.getId());
        } catch (Exception e) {
            LOG.error("An error occurred while creating user profile.", e);
        }
    }

    @Override
    public Optional<UserProfile> getProfileById(int id) {
        try {
            Optional<UserProfile> userProfileOptional = userProfileRepository.findById(id);

            if (userProfileOptional.isPresent()) {
                LOG.info("User profile retrieved successfully. ID: {}", id);
            } else {
                LOG.info("User profile not found for ID: {}", id);
            }

            return userProfileOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving user profile with ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserProfile> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        if(!userProfiles.isEmpty()) {
            LOG.info("Found {} users in the database", userProfiles.size());
        } else {
            LOG.info("No users found in the database");
        }

        return userProfiles;
    }

    @Override
    public void updateUserProfile(int profileId, Map<String, String> updatedData) {
        Optional<UserProfile> existingProfileOptional = getProfileById(profileId);

        if (existingProfileOptional.isPresent()) {
            UserProfile profile = existingProfileOptional.get();

            try {
                if (updatedData.containsKey("firstName")) {
                    profile.setFirstName(updatedData.get("firstName"));
                }
                if (updatedData.containsKey("lastName")) {
                    profile.setLastName(updatedData.get("lastName"));
                }
                if (updatedData.containsKey("contactNumber")) {
                    profile.setContactNumber(updatedData.get("contactNumber"));
                }
                if (updatedData.containsKey("address")) {
                    profile.setAddress(updatedData.get("address"));
                }

                userProfileRepository.save(profile);

                LOG.info("Profile with ID {} updated successfully", profileId);
            } catch (Exception e) {
                LOG.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    public Optional<UserProfile> getProfileByUsername(String username) {
        try {
            Optional<UserProfile> userProfileOptional = userProfileRepository.findByUsername(username);

            if (userProfileOptional.isPresent()) {
                LOG.info("User profile found for username: {}", username);
            } else {
                LOG.info("User profile not found for username: {}", username);
            }

            return userProfileOptional;
        } catch (Exception e) {
            LOG.error("Error fetching user profile for username {}: {}", username, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteUserProfile(int userId) {
        try {
            int profileId = userProfileRepository.getProfileId(userId);
            userProfileRepository.deleteById(profileId);
            LOG.info("User profile with ID {} deleted successfully.", profileId);
        } catch (Exception e) {
            LOG.error("Error deleting user profile for user ID {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userProfileRepository.existsByUsername(username);
    }

    @Override
    public Map<String, Object> getEmailTokens(String templateName, int userId, boolean includeUsername, boolean includeDate) {
        try {
            Optional<Map<String, Object>> tokensOptional = userProfileRepository.getEmailTokens(userId);
            Map<String, Object> tokens = tokensOptional.orElseGet(Collections::emptyMap);

            Map<String, Object> modifiedTokens = new HashMap<>(tokens);

            if ("Account Deletion".equals(templateName) && includeDate) {
                String date = userService.getDate(userId, "deletion_date");
                modifiedTokens.put("date", date);
            } else if ("Account Update".equals(templateName) && includeDate) {
                String date = userService.getDate(userId, "updated_at");
                modifiedTokens.put("date", date);
            }

            modifiedTokens.put("templateName", templateName);

            if (!includeUsername) {
                modifiedTokens.remove("username");
            }

            LOG.info("Generated tokens for user ID {}: {}", userId, modifiedTokens);
            return modifiedTokens;
        } catch (Exception e) {
            LOG.error("Error generating email tokens for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }
}