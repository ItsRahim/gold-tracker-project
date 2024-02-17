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

/**
 * This service class is responsible for updating accounts.
 * It implements the IAccountUpdateService interface.
 */
@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;

    /**
     * Updates the account with the given ID using the provided updated data.
     *
     * @param accountId The ID of the account to be updated.
     * @param updatedData The map containing the updated data.
     * @throws RuntimeException If an error occurs while updating the account.
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
            LOG.info("Account with email {} and ID {} updated successfully", account.getEmail(), accountId);

        } catch (DataException | EmailTokenException e) {
            LOG.error("Error updating account with email {} and ID {}: {}", account.getEmail(), accountId, e.getMessage());
            throw new RuntimeException("Failed to update account.", e);
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
        Optional<Account> accountOptional = accountRepositoryHandler.findById(accountId);

        return accountOptional.orElseThrow(() -> {
            LOG.warn("Account with ID {} not found.", accountId);
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
            emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), accountId, true, true, oldEmail);
        } catch (EmailTokenException e) {
            LOG.error("Error generating email tokens for account with ID {}", accountId, e);
            throw e;
        }
    }

    /**
     * Updates the email of the given account with the new email from the updated data.
     *
     * @param account The account to be updated.
     * @param updatedData The map containing the updated data.
     */
    private void updateEmail(Account account, Map<String, String> updatedData) {
        if (isEmailUpdateValid(updatedData)) {
            account.setEmail(updatedData.get("email"));
        } else {
            LOG.warn("Cannot update email for account with ID {}. Email {} already exists", account.getId(), updatedData.get("email"));
        }
    }

    /**
     * Updates the password of the given account with the new password hash from the updated data.
     *
     * @param account The account to be updated.
     * @param updatedData The map containing the updated data.
     */
    private void updatePassword(Account account, Map<String, String> updatedData) {
        if (isPasswordUpdateValid(updatedData)) {
            account.setPasswordHash(updatedData.get("passwordHash"));
        } else {
            LOG.warn("Cannot update password for account with ID {}", account.getId());
        }
    }

    /**
     * Checks if the email update is valid.
     *
     * @param updatedData The map containing the updated data.
     * @return true if the email update is valid, false otherwise.
     */
    private boolean isEmailUpdateValid(Map<String, String> updatedData) {
        String newEmail = updatedData.get("email");

        return updatedData.containsKey("email") &&
                !accountRepositoryHandler.hasAccount(newEmail) &&
                isNotEmpty(newEmail);
    }

    /**
     * Checks if the password update is valid.
     *
     * @param updatedData The map containing the updated data.
     * @return true if the password update is valid, false otherwise.
     */
    private boolean isPasswordUpdateValid(Map<String, String> updatedData) {
        String newPasswordHash = updatedData.get("passwordHash");

        return updatedData.containsKey("passwordHash") && isNotEmpty(newPasswordHash);
    }

    /**
     * Checks if the given string is not empty.
     *
     * @param value The string to be checked.
     * @return true if the string is not empty, false otherwise.
     */
    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

}
