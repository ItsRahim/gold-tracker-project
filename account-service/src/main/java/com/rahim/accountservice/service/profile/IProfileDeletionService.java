package com.rahim.accountservice.service.profile;

/**
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
public interface IProfileDeletionService {

    /**
     * Deletes a profile for the given user id.
     *
     * @param accountId The account id for the profile to be deleted
     */
    void deleteProfile(int accountId);
}
