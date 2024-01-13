package com.rahim.accountservice.service.profile.implementation;

import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileDeletionService implements IProfileDeletionService {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileQueryService.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;

    @Override
    @Transactional
    public void deleteProfile(int userId) {
        try {
            int profileId = profileRepositoryHandler.getProfileIdByUserId(userId);
            profileRepositoryHandler.deleteProfile(profileId);
            LOG.info("Account profile with ID {} deleted successfully.", profileId);
        } catch (Exception e) {
            LOG.error("Error deleting user profile for user ID {}: {}", userId, e.getMessage(), e);
        }
    }
}
