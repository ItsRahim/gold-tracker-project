package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
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

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDeletionService.class);

    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProfile(int userId) {
        try {
            int profileId = profileRepositoryHandler.getProfileIdByUserId(userId);
            profileRepositoryHandler.deleteProfile(profileId);
        } catch (Exception e) {
            LOG.error("Error deleting user profile for user ID {}: {}", userId, e.getMessage(), e);
        }
    }
}
