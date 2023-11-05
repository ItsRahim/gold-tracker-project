package com.rahim.userservice.service;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.enums.AuditAction;
import com.rahim.userservice.model.AuditLog;
import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final IAuditLog auditLog;
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

        if(!users.isEmpty()) {
            log.info("Found {} users in the database", users.size());
        } else {
            log.info("No users found in the database");
        }

        return users;
    }

    @Override
    public boolean deleteUserRequest(int userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String accountStatus = user.getAccountStatus();

            if (accountStatus.equals(AccountState.ACTIVE.getStatus())) {
                OffsetDateTime deletionDate = OffsetDateTime.now().plusDays(30);

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

    //TODO: Check if details have been updated before persisting
    @Override
    public void updateUser(User newUser) {
        userRepository.findById(newUser.getId()).ifPresent(existingUser -> {
            existingUser.setEmail(newUser.getEmail());
            existingUser.setPasswordHash(newUser.getPasswordHash());
            userRepository.save(existingUser);
            auditLog.updateAuditLog(existingUser, newUser, AuditAction.UPDATE.getAction());
        });
    }
}