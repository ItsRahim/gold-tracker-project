package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.json.ProfileJson;
import com.rahim.accountservice.service.profile.IProfileUpdateService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
    public Profile updateProfile(int profileId, Map<String, String> updatedData) {
        Profile profile = profileRepositoryHandler.findById(profileId);

        if (profile.getId() == null) {
            LOG.warn("Profile with ID {} not found.", profileId);
            throw new EntityNotFoundException("Profile does not exist. Unable to update");
        }

        try {
            updateProfileData(profile, updatedData);
            profileRepositoryHandler.updateProfile(profile);

            return profile;
        } catch (DataAccessException e) {
            LOG.error("Error updating profile: {}", e.getMessage());
            throw new DatabaseException("An unexpected error occurred whilst updating profile");
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
