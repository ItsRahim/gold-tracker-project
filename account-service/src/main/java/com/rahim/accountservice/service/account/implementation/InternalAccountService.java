package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.enums.AccountState;
import com.rahim.accountservice.enums.TemplateNameEnum;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternalAccountService implements IInternalAccountService {
    private static final Logger LOG = LoggerFactory.getLogger(InternalAccountService.class);
    private final IAccountDeletionService accountDeletionService;
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;
    private final IProfileDeletionService profileDeletionService;

    @Override
    public void runCleanupJob() {
        findAllInactiveUsers();
        processInactiveUsers();
        processPendingDeleteUsers();
    }

    private void deleteUserAccount(int userId) {
        try {
            emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_DELETED.getTemplateName(), userId, true, false);

            profileDeletionService.deleteProfile(userId);
            accountDeletionService.deleteAccount(userId);

            LOG.info("Account account with ID {} deleted successfully.", userId);

        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", userId, e.getMessage());
        }
    }

    private void findAllInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);

            List<Account> inactiveAccounts = accountRepositoryHandler.getInactiveUsers(cutoffDate);

            if (!inactiveAccounts.isEmpty()) {
                LOG.info("Found {} inactive users.", inactiveAccounts.size());

                for (Account account : inactiveAccounts) {
                    account.setAccountStatus(AccountState.INACTIVE.getStatus());
                    account.setCredentialsExpired(true);
                    accountRepositoryHandler.saveAccount(account);

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
            List<Account> pendingDeleteAccounts = accountRepositoryHandler.getPendingDeleteUsers();
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

            List<Account> usersToDelete = accountRepositoryHandler.getUsersToDelete(cutoffDate);

            if (!usersToDelete.isEmpty()) {
                LOG.info("Found {} users to be deleted.", usersToDelete.size());

                for (Account account : usersToDelete) {
                    accountDeletionService.requestAccountDelete(account.getId());
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