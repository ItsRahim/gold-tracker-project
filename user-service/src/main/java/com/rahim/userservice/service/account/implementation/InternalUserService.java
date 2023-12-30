package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.account.IInternalUserService;
import com.rahim.userservice.service.account.IUserService;
import com.rahim.userservice.service.profile.IUserProfileService;
import com.rahim.userservice.util.IEmailTokenGenerator;
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
    private final AccountRepository userRepository;
    private final IEmailTokenGenerator emailTokenGenerator;

    @Override
    public void deleteUserAccount(int userId) {
        try {
            emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_DELETED.getTemplateName(), userId, true, false);

            userProfileService.deleteUserProfile(userId);
            userService.deleteUserAccount(userId);

            LOG.info("Account account with ID {} deleted successfully.", userId);

        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", userId, e.getMessage());
        }
    }

    @Override
    public void runCleanupJob() {
        findAllInactiveUsers();
        processInactiveUsers();
        processPendingDeleteUsers();
    }

    private void findAllInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);

            List<Account> inactiveAccounts = userRepository.findInactiveUsers(cutoffDate);

            if (!inactiveAccounts.isEmpty()) {
                LOG.info("Found {} inactive users.", inactiveAccounts.size());

                for (Account account : inactiveAccounts) {
                    account.setAccountStatus(AccountState.INACTIVE.getStatus());
                    account.setCredentialsExpired(true);
                    userRepository.save(account);

                    emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_INACTIVITY.getTemplateName(), account.getId(), false, false);
                }

                LOG.info("Inactive users found. Account status successfully updated");
            } else {
                LOG.info("No inactive users found for deletion");
            }
        } catch (Exception e) {
            LOG.error("An error has occurred during the deletion request process: {}", e.getMessage());
        }
    }

    private void processPendingDeleteUsers() {
        try {
            List<Account> pendingDeleteAccounts = userRepository.findPendingDeleteUsers();
            LocalDate currentDate = LocalDate.now();

            if (!pendingDeleteAccounts.isEmpty()) {
                LOG.info("Found {} users pending deletion.", pendingDeleteAccounts.size());

                for (Account account : pendingDeleteAccounts) {
                    if (account.getDeleteDate() != null && account.getDeleteDate().isEqual(currentDate)) {
                        LOG.info("Deleting account account with ID: {}", account.getId());
                        deleteUserAccount(account.getId());
                    } else {
                        LOG.info("Skipping account with ID: {} as delete date is not today.", account.getId());
                    }
                }
            } else {
                LOG.info("No users found for deletion.");
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the cleanup of user accounts pending deletion: {}", e.getMessage());
        }
    }

    private void processInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(44);

            List<Account> usersToDelete = userRepository.findUsersToDelete(cutoffDate);

            if (!usersToDelete.isEmpty()) {
                LOG.info("Found {} users to be deleted.", usersToDelete.size());

                for (Account account : usersToDelete) {
                    userService.deleteUserRequest(account.getId());
                    LOG.info("Sending delete request to account with ID: {}", account.getId());
                }

                LOG.info("Account deletion requests sent successfully");
            } else {
                LOG.info("No users found for deletion requests");
            }
        } catch (Exception e) {
            LOG.error("An error occurred during the user deletion request process: {}", e.getMessage());
        }
    }
}
