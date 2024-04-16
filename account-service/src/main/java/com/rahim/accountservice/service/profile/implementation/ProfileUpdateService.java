package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.rahim.accountservice.model.request.ProfileRequestParam.*;

/**
 * Service for updating Profiles table
 *
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
                profileRepositoryHandler.saveProfile(profile);
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

    /**
     * Updates the data of a profile.
     *
     * @param profile The profile to update.
     * @param updatedData The new data for the profile.
     */
    private void updateProfileData(Profile profile, Map<String, String> updatedData) {
        if (updatedData.containsKey(PROFILE_FIRST_NAME)) {
            profile.setFirstName(updatedData.get(PROFILE_FIRST_NAME));
        }
        if (updatedData.containsKey(PROFILE_LAST_NAME)) {
            profile.setLastName(updatedData.get(PROFILE_LAST_NAME));
        }
        if (updatedData.containsKey(PROFILE_CONTACT_NUMBER)) {
            profile.setContactNumber(updatedData.get(PROFILE_CONTACT_NUMBER));
        }
        if (updatedData.containsKey(PROFILE_ADDRESS)) {
            profile.setAddress(updatedData.get(PROFILE_ADDRESS));
        }
    }
}
