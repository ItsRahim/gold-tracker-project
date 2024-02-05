package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.Profile;
import com.rahim.accountservice.service.profile.IProfileCreationService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileCreationService implements IProfileCreationService {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileCreationService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    public void createProfile(Account account, Profile profile) {
        try {
            profile.setAccount(account);
            profileRepositoryHandler.saveProfile(profile);
            LOG.info("Profile created successfully. ID: {}", profile.getId());
        } catch (Exception e) {
            LOG.error("An error occurred while creating a profile.", e);
        }
    }
}