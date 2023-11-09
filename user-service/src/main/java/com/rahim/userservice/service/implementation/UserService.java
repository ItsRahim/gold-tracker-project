package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public void createUserAndProfile(UserRequest userRequest) throws Exception {
        User user = userRequest.getUser();
        UserProfile userProfile = userRequest.getUserProfile();

        try {
            userRepository.save(user);

            userProfile.setUser(user);
            userProfileService.createUserProfile(userProfile);

            log.info("Successfully created User and User Profile for: {}", userProfile.getUsername());
        } catch (DataIntegrityViolationException e) {
            log.error("Error creating User and User Profile: {}", e.getMessage());
            throw new DataIntegrityViolationException("Error creating User and User Profile.");
        } catch (Exception e) {
            log.error("Unexpected error creating User and User Profile: {}", e.getMessage());
            throw new Exception("Unexpected error creating User and User Profile.");
        }
    }

    @Override
    public Optional<User> findUserById(int userId) throws Exception {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            log.error("Error while finding a user with ID: {}", userId, e);
            throw new Exception("Error finding a user by ID");
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();

        if (!users.isEmpty()) {
            log.info("Found {} users in the database", users.size());
        } else {
            log.info("No users found in the database");
        }

        return users;
    }

    @Override
    public boolean deleteUserRequest(int userId) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            String accountStatus = user.getAccountStatus();

            if (accountStatus.equals(AccountState.ACTIVE.getStatus())) {
                OffsetDateTime deletionDate = OffsetDateTime.now().plusDays(30).truncatedTo(ChronoUnit.SECONDS);

                user.setAccountStatus(AccountState.PENDING_DELETE.getStatus());
                user.setAccountLocked(true);
                user.setNotificationSetting(false);
                user.setDeleteDate(deletionDate);

                userRepository.save(user);

                log.info("User with ID {} is pending deletion on {}", userId, deletionDate);

                return true;
            } else {
                log.info("User with ID {} is not eligible for deletion", userId);
            }
        } else {
            log.warn("User with ID {} not found.", userId);
        }

        return false;
    }

    @Override
    public void updateUser(int userId, Map<String, String> updatedData) throws Exception {
        Optional<User> existingUserOptional = findUserById(userId);

        if (existingUserOptional.isPresent()) {
            User user = existingUserOptional.get();

            try {
                if (updatedData.containsKey("email")) {
                    user.setEmail(updatedData.get("email"));
                }
                if (updatedData.containsKey("passwordHash")) {
                    user.setPasswordHash(updatedData.get("passwordHash"));
                }

                userRepository.save(user);

                log.info("User with ID {} updated successfully", userId);
            } catch (Exception e) {
                log.error("Error updating user with ID {}: {}", userId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            log.warn("User with ID {} not found.", userId);
            throw new RuntimeException("User not found.");
        }
    }
}