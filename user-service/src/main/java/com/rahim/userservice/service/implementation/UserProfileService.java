package com.rahim.userservice.service.implementation;

import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private static final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    @Override
    public void createUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    @Override
    public Optional<UserProfile> getProfileById(int id) {
        return userProfileRepository.findById(id);
    }

    @Override
    public List<UserProfile> getAllProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        if(!userProfiles.isEmpty()) {
            log.info("Found {} users in the database", userProfiles.size());
        } else {
            log.info("No users found in the database");
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
                    profile.setFirstName(updatedData.get("firstName"));
                }
                if (updatedData.containsKey("contactNumber")) {
                    profile.setContactNumber(updatedData.get("contactNumber"));
                }
                if (updatedData.containsKey("address")) {
                    profile.setAddress(updatedData.get("address"));
                }

                userProfileRepository.save(profile);

                log.info("Profile with ID {} updated successfully", profileId);
            } catch (Exception e) {
                log.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            log.warn("Profile with ID {} not found.", profileId);
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    public Optional<UserProfile> getProfileByUsername(String username) {
        try {
            Optional<UserProfile> userProfileOptional = userProfileRepository.findByUsername(username);

            if (userProfileOptional.isPresent()) {
                log.info("User profile found for username: {}", username);
            } else {
                log.info("User profile not found for username: {}", username);
            }

            return userProfileOptional;
        } catch (Exception e) {
            log.error("Error fetching user profile for username {}: {}", username, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public void deleteUserProfile(int userId) {
        try {
            int profileId = userProfileRepository.getProfileId(userId);
            userProfileRepository.deleteById(profileId);
            log.info("User profile with ID {} deleted successfully.", profileId);
        } catch (Exception e) {
            log.error("Error deleting user profile for user ID {}: {}", userId, e.getMessage(), e);
        }
    }
}
