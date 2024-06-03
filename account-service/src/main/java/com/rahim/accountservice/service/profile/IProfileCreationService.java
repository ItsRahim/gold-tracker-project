package com.rahim.accountservice.service.profile;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.entity.Profile;

/**
 * @author Rahim Ahmed
 * @created 13/01/2024
 */
public interface IProfileCreationService {

    /**
     * Creates a profile for the given account.
     *
     * @param account The account for which the profile is to be created.
     * @param profile The profile to be created.
     */
    void createProfile(Account account, Profile profile);
}
