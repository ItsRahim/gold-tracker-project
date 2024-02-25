package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.enums.AccountState;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.rahim.accountservice.constant.EmailTemplates.ACCOUNT_DELETION_TEMPLATE;

/**
 * This service class is responsible for deleting accounts.
 * It implements the IAccountDeletionService interface.
 *
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDeletionService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;

    /**
     * @see IAccountDeletionService
     */
    @Override
    public boolean requestAccountDelete(int accountId) {
        Optional<Account> existingUserOptional = accountRepositoryHandler.findById(accountId);

        if (existingUserOptional.isPresent()) {
            Account account = existingUserOptional.get();

            String accountStatus = account.getAccountStatus();

            if (accountStatus.equals(AccountState.ACTIVE.getStatus())) {
                LocalDate deletionDate = LocalDate.now().plusDays(30);

                account.setAccountStatus(AccountState.PENDING_DELETE.getStatus());
                account.setAccountLocked(true);
                account.setNotificationSetting(false);
                account.setDeleteDate(deletionDate);

                try {
                    accountRepositoryHandler.saveAccount(account);
                    emailTokenGenerator.generateEmailTokens(ACCOUNT_DELETION_TEMPLATE, accountId, true, true);
                    LOG.info("Account with email {} and ID {} is pending deletion on {}", account.getEmail(), accountId, deletionDate);

                    return true;
                } catch (DataAccessException e) {
                    LOG.error("Error updating account with email {} and ID {}: {}", account.getEmail(), accountId, e.getMessage());
                    throw new RuntimeException("Failed to update account.", e);
                }
            } else {
                LOG.info("Account with email {} and ID {} is not eligible for deletion", account.getEmail(), accountId);
            }
        } else {
            LOG.warn("Account with ID {} not found.", accountId);
        }

        return false;
    }

}
