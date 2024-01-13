package com.rahim.accountservice.service.profile;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.Profile;

public interface IProfileCreationService {
    void createProfile(Account account, Profile profile);
}
