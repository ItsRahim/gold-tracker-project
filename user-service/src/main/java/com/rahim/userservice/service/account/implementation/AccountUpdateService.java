package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.exception.UserNotFoundException;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.service.account.IAccountUpdateService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import com.rahim.userservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
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
        Optional<Account> accountOptional = accountRepositoryHandler.findById(accountId);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            String oldEmail = account.getEmail();

            try {
                if (updatedData.containsKey("email")) {
                    account.setEmail(updatedData.get("email"));
                }
                if (updatedData.containsKey("passwordHash")) {
                    account.setPasswordHash(updatedData.get("passwordHash"));
                }

                accountRepositoryHandler.saveAccount(account);
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName(), accountId, true, true, oldEmail);
                LOG.info("Account with ID {} updated successfully", accountId);
            } catch (Exception e) {
                LOG.error("Error updating account with ID {}: {}", accountId, e.getMessage());
                throw new RuntimeException("Failed to update account.", e);
            }
        } else {
            LOG.warn("Account with ID {} not found.", accountId);
            throw new UserNotFoundException("Account with ID " + accountId + " not found");
        }
    }
}
