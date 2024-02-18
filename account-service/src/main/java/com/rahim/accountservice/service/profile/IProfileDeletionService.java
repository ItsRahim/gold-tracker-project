package com.rahim.accountservice.service.profile;

public interface IProfileDeletionService {

    /**
     * Deletes a profile for the given user id.
     *
     * @param userId The user id for the profile to be deleted
     */
    void deleteProfile(int userId);
}
