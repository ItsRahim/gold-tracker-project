package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.json.ProfileJson;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


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
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> updateProfile(int profileId, Map<String, String> updatedData) {
        Profile profile = profileRepositoryHandler.findById(profileId);

        if (profile.getId() == null) {
            LOG.warn("Profile with ID {} not found.", profileId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile does not exist. Unable to update");
        }

        try {
            updateProfileData(profile, updatedData);
            profileRepositoryHandler.updateProfile(profile);
            LOG.info("Profile with ID {} updated successfully", profileId);
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        } catch (Exception e) {
            LOG.error("Error updating profile with ID {}: {}", profileId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Unable to update profile");
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
