package com.rahim.accountservice.service.profile;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IProfileDeletionService {

    /**
     * Deletes a profile for the given user id.
     *
     * @param userId The user id for the profile to be deleted
     */
    void deleteProfile(int userId);
}
