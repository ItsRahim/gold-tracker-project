package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.constant.AccountState;
import com.rahim.userservice.entity.Account;
import com.rahim.userservice.model.EmailProperty;
import com.rahim.userservice.service.account.IAccountDeletionService;
import com.rahim.userservice.service.account.IInternalAccountService;
import com.rahim.userservice.service.profile.IProfileDeletionService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import com.rahim.userservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.common.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 15/11/2023
 */
@Service
@RequiredArgsConstructor
public class InternalAccountService implements IInternalAccountService {

    private static final Logger log = LoggerFactory.getLogger(InternalAccountService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IAccountDeletionService accountDeletionService;
    private final IProfileDeletionService profileDeletionService;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

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
            sendEmail(accountId, EmailTemplate.ACCOUNT_DELETED, true);
            profileDeletionService.deleteProfile(accountId);
            accountRepositoryHandler.deleteAccount(accountId);
            hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_SET, accountId);

            log.debug("Account with ID {} deleted successfully.", accountId);
        } catch (Exception e) {
            log.error("An error occurred deleting user: {}", e.getMessage(), e);
        }
    }

    /**
     * Finds all inactive users in the system and updates their account status to INACTIVE.
     * This method also generates email tokens for each inactive user.
     *
     * @throws RuntimeException If an error occurs while finding inactive users or updating their status.
     */
    private void findAndUpdateInactiveUsers() {
        LocalDate cutoffDate = DateTimeUtil.getLocalDate().minusDays(30);
        List<Account> inactiveAccounts = accountRepositoryHandler.getInactiveUsers(cutoffDate);

        if (inactiveAccounts.isEmpty()) {
            log.info("No inactive users found for deletion");
            return;
        }

        inactiveAccounts.forEach(account -> {
            account.setAccountStatus(AccountState.INACTIVE);
            account.setCredentialsExpired(true);
            accountRepositoryHandler.saveAccount(account);
            sendEmail(account.getId(), EmailTemplate.ACCOUNT_INACTIVITY, false);
        });

        log.debug("Inactive users found. Account status successfully updated");
    }

    /**
     * Processes all user accounts that are pending deletion. If the delete date of an account is the current date,
     * the account is deleted. Otherwise, the account is skipped.
     *
     * @throws RuntimeException If an error occurs while processing the user accounts pending deletion.
     */
    private void processPendingDeleteUsers() {
        try {
            LocalDate currentDate = DateTimeUtil.getLocalDate();
            List<Integer> accountIdsToDelete = accountRepositoryHandler.getUsersPendingDeletion(currentDate);

            if (accountIdsToDelete.isEmpty()) {
                log.info("No users found for deletion.");
                return;
            }

            log.info("Found {} users pending deletion.", accountIdsToDelete.size());
            for (Integer accountId : accountIdsToDelete) {
                deleteUserAccount(accountId);
            }

        } catch (Exception e) {
            log.error("Error processing pending delete users: {}", e.getMessage(), e);
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
            LocalDate cutoffDate = DateTimeUtil.getLocalDate().minusDays(44);
            List<Integer> accountIdsToDelete = accountRepositoryHandler.getUsersToDelete(cutoffDate);

            if (accountIdsToDelete.isEmpty()) {
                log.info("No users found for deletion requests");
                return;
            }

            for (Integer accountId : accountIdsToDelete) {
                accountDeletionService.requestAccountDelete(accountId);
            }

            log.info("Deletion requests sent for {} inactive users.", accountIdsToDelete.size());
        } catch (Exception e) {
            log.error("An error occurred processing inactive users: {}", e.getMessage());
        }
    }

    private void sendEmail(Integer accountId, EmailTemplate emailTemplate, boolean includeUsername) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(emailTemplate)
                .includeUsername(includeUsername)
                .includeDate(false)
                .build();
        emailTokenGenerator.generateEmailTokens(emailProperty);
    }

}
