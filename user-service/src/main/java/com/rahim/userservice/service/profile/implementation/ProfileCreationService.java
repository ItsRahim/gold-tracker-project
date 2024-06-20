package com.rahim.userservice.service.profile.implementation;

import com.rahim.userservice.entity.Account;
import com.rahim.userservice.entity.Profile;
import com.rahim.userservice.service.profile.IProfileCreationService;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for creating profiles.
 *
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileCreationService implements IProfileCreationService {

    private static final Logger log = LoggerFactory.getLogger(ProfileCreationService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    public void createProfile(Account account, Profile profile) {
        try {
            profile.setAccount(account);
            profileRepositoryHandler.createNewProfile(profile);
        } catch (Exception e) {
            log.error("An error occurred while creating a profile: {}", e.getMessage(), e);
        }
    }
}
