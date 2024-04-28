package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.dao.AccountDataAccess;
import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 15/11/2023
 */
@Service
@RequiredArgsConstructor
public class InternalAccountService implements IInternalAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(InternalAccountService.class);
    private final IAccountDeletionService accountDeletionService;
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final IProfileDeletionService profileDeletionService;

    /**
     * @see IInternalAccountService
     */
    @Override
    public void runCleanupJob() {
        findAndUpdateInactiveUsers();
        processInactiveUsers();
        processPendingDeleteUsers();
    }

    /**
     * Deletes the user account with the given ID.
     *
     * @param userId The ID of the user account to be deleted.
     * @throws RuntimeException If an error occurs while deleting the user account.
     */
    private void deleteUserAccount(int userId) {
        try {
            EmailProperty emailProperty = EmailProperty.builder()
                    .accountId(userId)
                    .templateName(EmailTemplate.ACCOUNT_DELETED_TEMPLATE)
                    .includeUsername(true)
                    .includeDate(false)
                    .build();
            emailTokenGenerator.generateEmailTokens(emailProperty);
            profileDeletionService.deleteProfile(userId);
            accountRepositoryHandler.deleteAccount(userId);

            LOG.debug("Account with ID {} deleted successfully.", userId);
        } catch (DataAccessException e) {
            handleDataAccessException("deleting", userId, e);
        }
    }

    /**
     * Finds all inactive users in the system and updates their account status to INACTIVE.
     * This method also generates email tokens for each inactive user.
     *
     * @throws RuntimeException If an error occurs while finding inactive users or updating their status.
     */
    private void findAndUpdateInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(30);
            List<Account> inactiveAccounts = accountRepositoryHandler.getInactiveUsers(cutoffDate);

            if (!inactiveAccounts.isEmpty()) {
                LOG.info("Found {} inactive users.", inactiveAccounts.size());

                inactiveAccounts.forEach(account -> {
                    account.setAccountStatus(AccountState.INACTIVE);
                    account.setCredentialsExpired(true);
                    accountRepositoryHandler.saveAccount(account);

                    EmailProperty emailProperty = EmailProperty.builder()
                            .accountId(account.getId())
                            .templateName(EmailTemplate.ACCOUNT_INACTIVITY_TEMPLATE)
                            .includeUsername(false)
                            .includeDate(false)
                            .build();
                    emailTokenGenerator.generateEmailTokens(emailProperty);
                });
                LOG.debug("Inactive users found. Account status successfully updated");
            } else {
                LOG.debug("No inactive users found for deletion");
            }
        } catch (DataAccessException e) {
            handleDataAccessException("finding and updating inactive", null, e);
        }
    }

    /**
     * Processes all user accounts that are pending deletion. If the delete date of an account is the current date,
     * the account is deleted. Otherwise, the account is skipped.
     *
     * @throws RuntimeException If an error occurs while processing the user accounts pending deletion.
     */
    private void processPendingDeleteUsers() {
        try {
            LocalDate currentDate = LocalDate.now();
            List<Tuple> pendingDeleteUsers = accountRepositoryHandler.getPendingDeleteUsers();

            if (!pendingDeleteUsers.isEmpty()) {
                LOG.info("Found {} users pending deletion.", pendingDeleteUsers.size());

                for (Tuple tuple : pendingDeleteUsers) {
                    Integer accountId = tuple.get(AccountDataAccess.COL_ACCOUNT_ID, Integer.class);
                    LocalDate deleteDate = tuple.get(AccountDataAccess.COL_ACCOUNT_DELETE_DATE, LocalDate.class);

                    if (accountId != null && deleteDate.isEqual(currentDate)) {
                        LOG.debug("Deleting account with ID: {}", accountId);
                        deleteUserAccount(accountId);
                    } else {
                        LOG.debug("Skipping account with ID: {} as delete date is not today.", deleteDate);
                    }
                }
            } else {
                LOG.info("No users found for deletion.");
            }
        } catch (DataAccessException e) {
            handleDataAccessException("processing pending delete", null, e);
        }
    }

    /**
     * Processes all inactive users in the system. If a user has been inactive for more than 44 days,
     * a request is sent to delete their account.
     *
     * @throws RuntimeException If an error occurs while processing the inactive users.
     */
    private void processInactiveUsers() {
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(44);
            List<Integer> accountIdsToDelete = accountRepositoryHandler.getUsersToDelete(cutoffDate);

            if (!accountIdsToDelete.isEmpty()) {
                for (Integer accountId : accountIdsToDelete) {
                    accountDeletionService.requestAccountDelete(accountId);
                    LOG.debug("Sending delete request to account with ID: {}", accountId);
                }

                LOG.debug("Account deletion requests sent successfully");
            } else {
                LOG.debug("No users found for deletion requests");
            }
        } catch (DataAccessException e) {
            handleDataAccessException("processing inactive", null, e);
        }
    }

    private void handleDataAccessException(String operation, Integer userId, DataAccessException e) {
        String errorMessage = String.format("Error %s user account", operation);
        if (userId != null) {
            errorMessage += String.format(" with ID %d", userId);
        }
        LOG.error("{}: {}", errorMessage, e.getMessage());
        throw new RuntimeException(errorMessage, e);
    }

}
