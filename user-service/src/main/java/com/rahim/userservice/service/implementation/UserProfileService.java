package com.rahim.userservice.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.kafka.IKafkaService;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.service.IUserProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserProfileService implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final IKafkaService kafkaService;
    private static final String SEND_EMAIL_TOPIC = "email-service-send-email";
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
    public void generateEmailTokens(String templateName, int userId, boolean includeUsername, boolean includeDate) {
        try {
            Optional<Map<String, Object>> emailDataOptional = userProfileRepository.getUserProfileDetails(userId);

            if (emailDataOptional.isPresent()) {
                Map<String, Object> emailData = new HashMap<>(emailDataOptional.get());

                updateMapKey(emailData, "first_name", "firstName");
                updateMapKey(emailData, "last_name", "lastName");
                updateMapKey(emailData, "delete_date", "deleteDate");
                updateMapKey(emailData, "updated_at", "updatedAt");

                if (!includeUsername) {
                    emailData.remove("username");
                }

                if (!includeDate) {
                    List<String> keysToRemove = Arrays.asList("deleteDate", "updatedAt");
                    emailData.keySet().removeAll(keysToRemove);
                }

                if (TemplateNameEnum.ACCOUNT_DELETION.getTemplateName().equals(templateName) && includeDate) {
                    emailData.remove("updatedAt");
                }

                if (TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName().equals(templateName) && includeDate) {
                    emailData.remove("deleteDate");
                }

                emailData.put("templateName", templateName);

                ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;
                String jsonEmailData = objectMapper.writeValueAsString(emailData);

                LOG.info("Generated tokens for user ID {}: {}", userId, jsonEmailData);

                kafkaService.sendMessage(SEND_EMAIL_TOPIC, jsonEmailData);
            } else {
                LOG.info("No email data found for user ID {}", userId);
            }
        } catch (Exception e) {
            LOG.error("Error generating email tokens for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    private void updateMapKey(Map<String, Object> map, String oldKey, String newKey) {
        if (map.containsKey(oldKey)) {
            Object value = map.remove(oldKey);

            map.put(newKey, value);
        }
    }
}