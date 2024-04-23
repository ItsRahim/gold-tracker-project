package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.exception.EmailTokenException;
import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.request.AccountJsonRequest;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;

    /**
     * @see IAccountUpdateService
     */
    @Override
    public void updateAccount(int accountId, Map<String, String> updatedData) {
        Account account = getAccountById(accountId);
        String oldEmail = account.getEmail();

        try {
            updateEmail(account, updatedData);
            updatePassword(account, updatedData);
            accountRepositoryHandler.saveAccount(account);
            generateEmailTokens(accountId, oldEmail);
        } catch (RuntimeException e) {
            LOG.error("Error updating account with ID {}: {}", accountId, e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves an account by its ID.
     *
     * @param accountId The ID of the account to be retrieved.
     * @return The account with the given ID.
     * @throws UserNotFoundException If the account with the given ID is not found.
     */
    private Account getAccountById(int accountId) {
        return accountRepositoryHandler.findById(accountId).orElseThrow(() -> {
            LOG.warn("Account with ID {} not found while updating.", accountId);
            return new UserNotFoundException("Account with ID " + accountId + " not found");
        });
    }

    /**
     * Generates email tokens for the given account ID and old email.
     *
     * @param accountId The ID of the account.
     * @param oldEmail The old email of the account.
     * @throws EmailTokenException If an error occurs while generating the email tokens.
     */
    private void generateEmailTokens(int accountId, String oldEmail) {
        try {
            emailTokenGenerator.generateEmailTokens(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE, accountId, true, true, oldEmail);
        } catch (EmailTokenException e) {
            LOG.error("Error generating email tokens for account with ID {}", accountId, e);
            throw new RuntimeException("Failed to generate email tokens for account.", e);
        }
    }

    private void updateEmail(Account account, Map<String, String> updatedData) {
        String newEmail = updatedData.get(AccountJsonRequest.ACCOUNT_EMAIL);
        if (isNotEmpty(newEmail) && !accountRepositoryHandler.existsByEmail(newEmail)) {
            account.setEmail(newEmail);
            LOG.debug("Email updated successfully");
        } else {
            LOG.debug("Email address update skipped for account with ID: {}", account.getId());
        }
    }

    private void updatePassword(Account account, Map<String, String> updatedData) {
        String newPasswordHash = updatedData.get(AccountJsonRequest.ACCOUNT_PASSWORD_HASH);
        if (isNotEmpty(newPasswordHash)) {
            account.setPasswordHash(newPasswordHash);
            LOG.debug("Password updated successfully");
        } else {
            LOG.debug("Password update skipped for account with ID: {}", account.getId());
        }
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

}
