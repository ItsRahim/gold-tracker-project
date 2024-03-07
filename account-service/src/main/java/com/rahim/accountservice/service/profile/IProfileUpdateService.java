package com.rahim.accountservice.service.profile;

import java.util.Map;

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
    void updateProfile(int profileId, Map<String, String> updatedData);
}
