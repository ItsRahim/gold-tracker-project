package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.model.User;
import com.rahim.userservice.repository.UserProfileRepository;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IInternalUserService;
import com.rahim.userservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalUserService implements IInternalUserService {
    private static final Logger log = LoggerFactory.getLogger(InternalUserService.class);
    private final IUserService userService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

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

    @Override
    public void processPendingDeleteUsers() {
        try {
            List<User> pendingDeleteUsers = userRepository.findPendingDeleteUsers();
            LocalDate currentDate = LocalDate.now();

            if (!pendingDeleteUsers.isEmpty()) {
                log.info("Found {} users pending deletion.", pendingDeleteUsers.size());

                for (User user : pendingDeleteUsers) {
                    if (user.getDeleteDate() != null && user.getDeleteDate().isEqual(currentDate)) {
                        log.info("Deleting user account with ID: {}", user.getId());
                        deleteUserAccount(user.getId());
                    } else {
                        log.info("Skipping user with ID: {} as delete date is not today.", user.getId());
                    }
                }
            } else {
                log.info("No users found for deletion.");
            }
        } catch (Exception e) {
            log.error("An error occurred during the cleanup of user accounts pending deletion: {}", e.getMessage());
        }
    }

    @Override
    public void findAllInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);

            List<User> inactiveUsers = userRepository.findInactiveUsers(cutoffDate);

            if (!inactiveUsers.isEmpty()) {
                log.info("Found {} inactive users.", inactiveUsers.size());

                for (User user : inactiveUsers) {
                    user.setAccountStatus(AccountState.INACTIVE.getStatus());
                    user.setCredentialsExpired(true);
                    userRepository.save(user);
                }

                log.info("Inactive user deletion process has been completed successfully");
            } else {
                log.info("No inactive users found for deletion");
            }
        } catch (Exception e) {
            log.error("An error has occurred during the deletion process: {}", e.getMessage());
        }
    }

    @Override
    public void processInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(44);

            List<User> usersToDelete = userRepository.findUsersToDelete(cutoffDate);

            if (!usersToDelete.isEmpty()) {
                log.info("Found {} users to be deleted.", usersToDelete.size());

                for (User user : usersToDelete) {
                    userService.deleteUserRequest(user.getId());
                    log.info("Sending delete request to user with ID: {}", user.getId());
                }

                log.info("User deletion requests sent successfully");
            } else {
                log.info("No users found for deletion requests");
            }
        } catch (Exception e) {
            log.error("An error occurred during the user deletion request process: {}", e.getMessage());
        }
    }
}
