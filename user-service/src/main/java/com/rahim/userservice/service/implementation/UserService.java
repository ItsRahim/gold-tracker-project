package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.exception.DuplicateUserException;
import com.rahim.userservice.exception.UserNotFoundException;
import com.rahim.userservice.model.User;
import com.rahim.userservice.model.UserProfile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IUserProfileService userProfileService;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Override
    @Transactional
    public void createUserAndProfile(UserRequest userRequest) throws Exception {
        User user = userRequest.getUser();
        UserProfile userProfile = userRequest.getUserProfile();

        String email = user.getEmail();
        String username = userProfile.getUsername();
        if (!userExist(email, username)) {
            try {
                userRepository.save(user);

                userProfile.setUser(user);
                userProfileService.createUserProfile(userProfile);

                log.info("Successfully created User and User Profile for: {}", userProfile.getUsername());
            } catch (DataIntegrityViolationException e) {
                log.error("Error creating User and User Profile. Data integrity violation: {}", e.getMessage());
                throw new DataIntegrityViolationException("Error creating User and User Profile.", e);
            } catch (Exception e) {
                log.error("Unexpected error creating User and User Profile: {}", e.getMessage());
                throw new Exception("Unexpected error creating User and User Profile.", e);
            }
        } else {
            log.warn("User with email {} or username {} already exists. Not creating duplicate.", email, username);
            throw new DuplicateUserException("User with email " + email + " or username " + username + " already exists.");
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
            log.error("Error while finding a user with ID: {}", userId, e);
            throw new UserNotFoundException("Error finding a user by ID");
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
                LocalDate deletionDate = LocalDate.now().plusDays(30);

                user.setAccountStatus(AccountState.PENDING_DELETE.getStatus());
                user.setAccountLocked(true);
                user.setNotificationSetting(false);
                user.setDeleteDate(deletionDate);

                userRepository.save(user);

                //TODO: Send email to user that there account WILL be deleted
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

                log.info("User with ID {} updated successfully", userId);
            } catch (Exception e) {
                log.error("Error updating user with ID {}: {}", userId, e.getMessage());
                throw new RuntimeException("Failed to update user.", e);
            }
        } else {
            log.warn("User with ID {} not found.", userId);
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
    }

    @Override
    public void deleteUserAccount(int userId) {
        try {
            userRepository.deleteById(userId);
            log.info("User account with ID {} deleted successfully.", userId);
        } catch (Exception e) {
            log.error("Error deleting user account with ID {}: {}", userId, e.getMessage(), e);
        }
    }
}