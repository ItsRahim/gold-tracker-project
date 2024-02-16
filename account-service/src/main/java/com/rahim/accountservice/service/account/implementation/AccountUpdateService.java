package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.enums.TemplateNameEnum;
import com.rahim.accountservice.exception.EmailTokenException;
import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;

    @Override
    public void updateAccount(int accountId, Map<String, String> updatedData) {
        Account account = getAccountById(accountId);
        String oldEmail = account.getEmail();

        try {
            updateEmail(account, updatedData);
            updatePassword(account, updatedData);
            saveAccount(account);
            generateEmailTokens(accountId, oldEmail);
            LOG.info("Account with email {} and ID {} updated successfully", account.getEmail(), accountId);

        } catch (DataException | EmailTokenException e) {
            LOG.error("Error updating account with email {} and ID {}: {}", account.getEmail(), accountId, e.getMessage());
            throw new RuntimeException("Failed to update account.", e);
        }
    }

    private Account getAccountById(int accountId) {
        Optional<Account> accountOptional = accountRepositoryHandler.findById(accountId);

        return accountOptional.orElseThrow(() -> {
            LOG.warn("Account with ID {} not found.", accountId);
            return new UserNotFoundException("Account with ID " + accountId + " not found");
        });
    }

    private void saveAccount(Account account) {
        try {
            accountRepositoryHandler.saveAccount(account);
        } catch (DataException e) {
            LOG.error("Error saving account with email {} and ID {} to the database", account.getEmail(), account.getId(), e);
            throw e;
        }
    }

    private void generateEmailTokens(int accountId, String oldEmail) {
        try {
            emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), accountId, true, true, oldEmail);
        } catch (EmailTokenException e) {
            LOG.error("Error generating email tokens for account with ID {}", accountId, e);
            throw e;
        }
    }

    private void updateEmail(Account account, Map<String, String> updatedData) {
        if (isEmailUpdateValid(updatedData)) {
            account.setEmail(updatedData.get("email"));
        } else {
            LOG.warn("Cannot update email for account with ID {}. Email {} already exists", account.getId(), updatedData.get("email"));
        }
    }

    private void updatePassword(Account account, Map<String, String> updatedData) {
        if (isPasswordUpdateValid(updatedData)) {
            account.setPasswordHash(updatedData.get("passwordHash"));
        } else {
            LOG.warn("Cannot update password for account with ID {}", account.getId());
        }
    }

    private boolean isEmailUpdateValid(Map<String, String> updatedData) {
        String newEmail = updatedData.get("email");

        return updatedData.containsKey("email") &&
                !accountRepositoryHandler.hasAccount(newEmail) &&
                isNotEmpty(newEmail);
    }

    private boolean isPasswordUpdateValid(Map<String, String> updatedData) {
        String newPasswordHash = updatedData.get("passwordHash");

        return updatedData.containsKey("passwordHash") && isNotEmpty(newPasswordHash);
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

}
