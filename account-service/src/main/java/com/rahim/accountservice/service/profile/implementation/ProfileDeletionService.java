package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for deleting profiles.
 *
 * @author Rahim Ahmed
 * @created 31/12/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileDeletionService implements IProfileDeletionService {

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
