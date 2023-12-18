package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.exception.DuplicateUserException;
import com.rahim.userservice.exception.UserNotFoundException;
import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IUserProfileService userProfileService;
    private final IEmailTokenGenerator emailTokenGenerator;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public void createUserAndProfile(UserRequest userRequest) throws DuplicateUserException {
        User user = userRequest.getUser();
        UserProfile userProfile = userRequest.getUserProfile();

        String email = user.getEmail();
        String username = userProfile.getUsername();

        boolean userExists = userExist(email, username);
        boolean anyNullUser = ObjectUtils.anyNull(user);
        boolean anyNullProfile = ObjectUtils.anyNull(userProfile);

        if (!userExists && (!anyNullUser || !anyNullProfile)) {
            try {
                userRepository.save(user);

                userProfile.setUser(user);
                userProfileService.createUserProfile(userProfile);

                LOG.info("Successfully created User and User Profile for: {}", userProfile.getUsername());
            } catch (DataIntegrityViolationException e) {
                LOG.error("Error creating User and User Profile. Data integrity violation: {}", e.getMessage());
                throw new DuplicateUserException("User with email " + email + " or username " + username + " already exists.", e);
            } catch (Exception e) {
                LOG.error("Unexpected error creating User and User Profile: {}", e.getMessage());
                throw new RuntimeException("Unexpected error creating User and User Profile.", e);
            }
        } else {
            LOG.warn("User with email {} or username {} already exists or null values found. Not creating duplicate.", email, username);
            throw new DuplicateUserException("User with email " + email + " or username " + username + " already exists or null values found.");
        }
    }

    private boolean userExist(String email, String username) {
        return userRepository.existsByEmail(email) || userProfileService.checkUsernameExists(username);
    }

    @Override
    public Optional<User> findUserById(int userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            LOG.error("Error while finding a user with ID: {}", userId, e);
            throw new UserNotFoundException("Error finding a user by ID");
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();

        if (!users.isEmpty()) {
            LOG.info("Found {} users in the database", users.size());
        } else {
            LOG.info("No users found in the database");
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
                LocalDate deletionDate = LocalDate.now().plusDays(30);

                user.setAccountStatus(AccountState.PENDING_DELETE.getStatus());
                user.setAccountLocked(true);
                user.setNotificationSetting(false);
                user.setDeleteDate(deletionDate);

                userRepository.save(user);
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_DELETION.getTemplateName(), userId, true, true);
                LOG.info("User with ID {} is pending deletion on {}", userId, deletionDate);

                return true;
            } else {
                LOG.info("User with ID {} is not eligible for deletion", userId);
            }
        } else {
            LOG.warn("User with ID {} not found.", userId);
        }

        return false;
    }

    @Override
    public void updateUser(int userId, Map<String, String> updatedData) {
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
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), userId, true, true);
                LOG.info("User with ID {} updated successfully", userId);
            } catch (Exception e) {
                LOG.error("Error updating user with ID {}: {}", userId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            LOG.warn("User with ID {} not found.", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    @Override
    public void deleteUserAccount(int userId) {
        try {
            userRepository.deleteById(userId);
            LOG.info("User account with ID {} deleted successfully.", userId);
        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", userId, e.getMessage(), e);
        }
    }
}