package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.json.ProfileJson;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileUpdateService implements IProfileUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileUpdateService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    public void updateProfile(int profileId, Map<String, String> updatedData) {
        Optional<Profile> profileOptional = profileRepositoryHandler.findById(profileId);

        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();

            try {
                updateProfileData(profile, updatedData);
                profileRepositoryHandler.updateProfile(profile);
                LOG.info("Profile with ID {} updated successfully", profileId);
            } catch (Exception e) {
                LOG.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
                throw new RuntimeException("Failed to update profile.", e);
            }
        } else {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new UserNotFoundException("Profile not found.");
        }
    }

    /**
     * Updates the data of a profile.
     *
     * @param profile     The profile to update.
     * @param updatedData The new data for the profile.
     */
    private void updateProfileData(Profile profile, Map<String, String> updatedData) {
        updatedData.forEach((key, value) -> {
            switch (key) {
                case ProfileJson.PROFILE_FIRST_NAME:
                    profile.setFirstName(value);
                    break;
                case ProfileJson.PROFILE_LAST_NAME:
                    profile.setLastName(value);
                    break;
                case ProfileJson.PROFILE_CONTACT_NUMBER:
                    profile.setContactNumber(value);
                    break;
                case ProfileJson.PROFILE_ADDRESS:
                    profile.setAddress(value);
                    break;
                default:
                    LOG.warn("Ignoring unknown key '{}' in updated data for profile with ID {}", key, profile.getId());
            }
        });
    }

}
