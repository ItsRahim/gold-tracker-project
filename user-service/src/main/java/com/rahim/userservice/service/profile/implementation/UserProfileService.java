package com.rahim.userservice.service.profile.implementation;

import com.rahim.userservice.model.Profile;
import com.rahim.userservice.repository.ProfileRepository;
import com.rahim.userservice.service.profile.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final ProfileRepository profileRepository;
    private static final Logger LOG = LoggerFactory.getLogger(UserProfileService.class);

    @Override
    public void createUserProfile(Profile profile) {
        try {
            profileRepository.save(profile);
            LOG.info("Account profile created successfully. ID: {}", profile.getId());
        } catch (Exception e) {
            LOG.error("An error occurred while creating user profile.", e);
        }
    }

    @Override
    public Optional<Profile> getProfileById(int id) {
        try {
            Optional<Profile> userProfileOptional = profileRepository.findById(id);

            if (userProfileOptional.isPresent()) {
                LOG.info("Account profile retrieved successfully. ID: {}", id);
            } else {
                LOG.info("Account profile not found for ID: {}", id);
            }

            return userProfileOptional;
        } catch (Exception e) {
            LOG.error("An error occurred while retrieving user profile with ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Profile> getAllProfiles() {
        List<Profile> profiles = profileRepository.findAll();

        if(!profiles.isEmpty()) {
            LOG.info("Found {} users in the database", profiles.size());
        } else {
            LOG.info("No users found in the database");
        }

        return profiles;
    }

    @Override
    public void updateUserProfile(int profileId, Map<String, String> updatedData) {
        Optional<Profile> existingProfileOptional = getProfileById(profileId);

        if (existingProfileOptional.isPresent()) {
            Profile profile = existingProfileOptional.get();

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

                profileRepository.save(profile);

                LOG.info("Profile with ID {} updated successfully", profileId);
            } catch (Exception e) {
                LOG.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new RuntimeException("Account not found.");
        }
    }

    @Override
    public Optional<Profile> getProfileByUsername(String username) {
        try {
            Optional<Profile> userProfileOptional = profileRepository.findByUsername(username);

            if (userProfileOptional.isPresent()) {
                LOG.info("Account profile found for username: {}", username);
            } else {
                LOG.info("Account profile not found for username: {}", username);
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
            int profileId = profileRepository.getProfileId(userId);
            profileRepository.deleteById(profileId);
            LOG.info("Account profile with ID {} deleted successfully.", profileId);
        } catch (Exception e) {
            LOG.error("Error deleting user profile for user ID {}: {}", userId, e.getMessage(), e);
        }
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return profileRepository.existsByUsername(username);
    }

    public Map<String, Object> getUserProfileDetails(int userId) {
        try {
            Optional<Map<String, Object>> userProfileOptional = profileRepository.getUserProfileDetails(userId);

            if (userProfileOptional.isPresent()) {
                Map<String, Object> userProfileDetails = userProfileOptional.get();
                LOG.info("Account profile details retrieved successfully for user ID {}: {}", userId, userProfileDetails);
                return userProfileDetails;
            } else {
                LOG.info("No user profile details found for user ID {}", userId);
                return null;
            }
        } catch (Exception e) {
            LOG.error("Error retrieving user profile details for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error while retrieving user profile details", e);
        }
    }

}