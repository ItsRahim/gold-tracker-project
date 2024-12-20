package com.rahim.userservice.service.profile.implementation;

import com.rahim.userservice.service.profile.IProfileDeletionService;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for deleting profiles.
 *
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Service
@RequiredArgsConstructor
public class ProfileDeletionService implements IProfileDeletionService {

    private static final Logger log = LoggerFactory.getLogger(ProfileDeletionService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProfile(int accountId) {
        try {
            int profileId = profileRepositoryHandler.getProfileIdByAccountId(accountId);
            profileRepositoryHandler.deleteProfile(profileId);
        } catch (Exception e) {
            log.error("Error deleting user profile for user ID {}: {}", accountId, e.getMessage(), e);
        }
    }
}
