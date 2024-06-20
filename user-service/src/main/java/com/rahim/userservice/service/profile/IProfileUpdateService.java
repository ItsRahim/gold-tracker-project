package com.rahim.userservice.service.profile;

import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.request.profile.ProfileUpdateRequest;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IProfileUpdateService {

    /**
     * Updates a profile with the given ID and data.
     *
     * @param profileId The ID of the profile to update.
     * @param updatedData The new data for the profile.
     */
    Profile updateProfile(int profileId, ProfileUpdateRequest updatedData);
}
