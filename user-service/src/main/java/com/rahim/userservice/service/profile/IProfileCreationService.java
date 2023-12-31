package com.rahim.userservice.service.profile;

import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;

public interface IProfileCreationService {
    void createProfile(Account account, Profile profile);
}
