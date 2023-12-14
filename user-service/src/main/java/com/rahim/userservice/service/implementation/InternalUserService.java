package com.rahim.userservice.service.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.model.User;
import com.rahim.userservice.repository.UserRepository;
import com.rahim.userservice.service.IInternalUserService;
import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.service.IUserService;
import com.rahim.userservice.util.KafkaDataUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalUserService implements IInternalUserService {
    private static final Logger LOG = LoggerFactory.getLogger(InternalUserService.class);
    private final IUserService userService;
    private final IUserProfileService userProfileService;
    private final UserRepository userRepository;
    private final KafkaDataUtil kafkaDataUtil;

    @Override
    public void deleteUserAccount(int userId) {
        try {
            userProfileService.deleteUserProfile(userId);
            userService.deleteUserAccount(userId);

            LOG.info("User account with ID {} deleted successfully.", userId);

            kafkaDataUtil.prepareEmailData(TemplateNameEnum.ACCOUNT_DELETED, userId);
        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", userId, e.getMessage());
        }
    }

    @Override
    public void findAllInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);

            List<User> inactiveUsers = userRepository.findInactiveUsers(cutoffDate);

            if (!inactiveUsers.isEmpty()) {
                LOG.info("Found {} inactive users.", inactiveUsers.size());

                for (User user : inactiveUsers) {
                    user.setAccountStatus(AccountState.INACTIVE.getStatus());
                    user.setCredentialsExpired(true);
                    userRepository.save(user);

                    kafkaDataUtil.prepareEmailData(TemplateNameEnum.ACCOUNT_INACTIVITY, user.getId());
                }

                LOG.info("Inactive users found. Account status successfully updated");
            } else {
                LOG.info("No inactive users found for deletion");
            }
        } catch (Exception e) {
            LOG.error("An error has occurred during the deletion request process: {}", e.getMessage());
        }
    }

    @Override
    public void processPendingDeleteUsers() {
        try {
            List<User> pendingDeleteUsers = userRepository.findPendingDeleteUsers();
            LocalDate currentDate = LocalDate.now();

            if (!pendingDeleteUsers.isEmpty()) {
                LOG.info("Found {} users pending deletion.", pendingDeleteUsers.size());

                for (User user : pendingDeleteUsers) {
                    if (user.getDeleteDate() != null && user.getDeleteDate().isEqual(currentDate)) {
                        LOG.info("Deleting user account with ID: {}", user.getId());
                        deleteUserAccount(user.getId());
                    } else {
                        LOG.info("Skipping user with ID: {} as delete date is not today.", user.getId());
                    }
                }
            } else {
                LOG.info("No users found for deletion.");
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the cleanup of user accounts pending deletion: {}", e.getMessage());
        }
    }

    @Override
    public void processInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(44);

            List<User> usersToDelete = userRepository.findUsersToDelete(cutoffDate);

            if (!usersToDelete.isEmpty()) {
                LOG.info("Found {} users to be deleted.", usersToDelete.size());

                for (User user : usersToDelete) {
                    userService.deleteUserRequest(user.getId());
                    LOG.info("Sending delete request to user with ID: {}", user.getId());
                }

                LOG.info("User deletion requests sent successfully");
            } else {
                LOG.info("No users found for deletion requests");
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the user deletion request process: {}", e.getMessage());
        }
    }
}
