package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.account.IInternalAccountService;
import com.rahim.accountservice.service.profile.IProfileDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 15/11/2023
 */
@Service
@RequiredArgsConstructor
public class InternalAccountService implements IInternalAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(InternalAccountService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IAccountDeletionService accountDeletionService;
    private final IProfileDeletionService profileDeletionService;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

    /**
     * @see IInternalAccountService
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void runCleanupJob() {
        findAndUpdateInactiveUsers();
        processInactiveUsers();
        processPendingDeleteUsers();
    }

    /**
     * Deletes the user account with the given ID.
     *
     * @param accountId The ID of the user account to be deleted.
     * @throws RuntimeException If an error occurs while deleting the user account.
     */
    private void deleteUserAccount(int accountId) {
        try {
            sendEmail(accountId, EmailTemplate.ACCOUNT_DELETED_TEMPLATE, true, false);
            profileDeletionService.deleteProfile(accountId);
            accountRepositoryHandler.deleteAccount(accountId);
            removeFromHazelcastSet(accountId);

            LOG.debug("Account with ID {} deleted successfully.", accountId);
        } catch (DataAccessException e) {
            LOG.error("An error occurred deleting user: {}", e.getMessage());
        }
    }

    private void removeFromHazelcastSet(int userId) {
        hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_SET, userId);
        LOG.debug("Removed deleted account from Hazelcast set");
    }

    /**
     * Finds all inactive users in the system and updates their account status to INACTIVE.
     * This method also generates email tokens for each inactive user.
     *
     * @throws RuntimeException If an error occurs while finding inactive users or updating their status.
     */
    private void findAndUpdateInactiveUsers() {
        LocalDate cutoffDate = generateDate().minusDays(30);
        List<Account> inactiveAccounts = accountRepositoryHandler.getInactiveUsers(cutoffDate);

        if (inactiveAccounts.isEmpty()) {
            LOG.debug("No inactive users found for deletion");
            return;
        }

        inactiveAccounts.forEach(account -> {
            account.setAccountStatus(AccountState.INACTIVE);
            account.setCredentialsExpired(true);
            accountRepositoryHandler.saveAccount(account);
            sendEmail(account.getId(), EmailTemplate.ACCOUNT_INACTIVITY_TEMPLATE, false, false);
        });

        LOG.debug("Inactive users found. Account status successfully updated");
    }

    /**
     * Processes all user accounts that are pending deletion. If the delete date of an account is the current date,
     * the account is deleted. Otherwise, the account is skipped.
     *
     * @throws RuntimeException If an error occurs while processing the user accounts pending deletion.
     */
    private void processPendingDeleteUsers() {
        try {
            LocalDate currentDate = generateDate();
            List<Integer> accountIdsToDelete = accountRepositoryHandler.getUsersPendingDeletion(currentDate);

            if (accountIdsToDelete.isEmpty()) {
                LOG.info("No users found for deletion.");
                return;
            }

            LOG.info("Found {} users pending deletion.", accountIdsToDelete.size());
            for (Integer accountId : accountIdsToDelete) {
                deleteUserAccount(accountId);
            }

        } catch (Exception e) {
            LOG.error("Error processing pending delete users: {}", e.getMessage(), e);
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
            LocalDate cutoffDate = generateDate().minusDays(44);
            List<Integer> accountIdsToDelete = accountRepositoryHandler.getUsersToDelete(cutoffDate);

            if (accountIdsToDelete.isEmpty()) {
                LOG.info("No users found for deletion requests");
                return;
            }

            for (Integer accountId : accountIdsToDelete) {
                accountDeletionService.requestAccountDelete(accountId);
            }

            LOG.info("Deletion requests sent for {} inactive users.", accountIdsToDelete.size());
        } catch (DataAccessException e) {
            LOG.error("An error occurred processing inactive users: {}", e.getMessage());
        }
    }

    private LocalDate generateDate() {
        return LocalDate.now(ZoneId.of("UTC"));
    }

    private void sendEmail(Integer accountId, String emailTemplate, boolean includeUsername, boolean includeDate) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(emailTemplate)
                .includeUsername(includeUsername)
                .includeDate(includeDate)
                .build();
        emailTokenGenerator.generateEmailTokens(emailProperty);
    }

}
