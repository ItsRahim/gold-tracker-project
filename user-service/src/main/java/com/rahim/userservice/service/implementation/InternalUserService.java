package com.rahim.userservice.service.implementation;

import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IInternalUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InternalUserService implements IInternalUserService {
    private static final Logger log = LoggerFactory.getLogger(InternalUserService.class);
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional
    @Override
    public void deleteUserAccount(int userId) {
        try {
            userProfileRepository.deleteUserProfileByUserId(userId);
            userRepository.deleteByUserId(userId);

            log.info("User account with ID {} deleted successfully.", userId);
        } catch (Exception e) {
            log.error("Error deleting user account with ID {}: {}", userId, e.getMessage());
        }
    }
}
