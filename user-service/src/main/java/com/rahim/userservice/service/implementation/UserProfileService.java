package com.rahim.userservice.service.implementation;

import com.rahim.userservice.model.User;
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
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public void deleteUserProfile(int userId) {
        int profileId = userProfileRepository.getProfileId(userId);
        userProfileRepository.deleteById(profileId);
    }
}
