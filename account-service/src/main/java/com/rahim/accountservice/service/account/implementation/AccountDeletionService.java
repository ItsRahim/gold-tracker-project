package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.enums.AccountState;
import com.rahim.accountservice.enums.TemplateNameEnum;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletionService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountDeletionService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IEmailTokenGenerator emailTokenGenerator;

    @Override
    public void deleteAccount(int accountId) {
        try {
            accountRepositoryHandler.deleteAccount(accountId);
            LOG.info("Account account with ID {} deleted successfully.", accountId);
        } catch (Exception e) {
            LOG.error("Error deleting user account with ID {}: {}", accountId, e.getMessage(), e);
        }
    }

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

                accountRepositoryHandler.saveAccount(account);
                emailTokenGenerator.generateEmailTokens(TemplateNameEnum.ACCOUNT_DELETION.getTemplateName(), accountId, true, true);
                LOG.info("Account with ID {} is pending deletion on {}", accountId, deletionDate);

                return true;
            } else {
                LOG.info("Account with ID {} is not eligible for deletion", accountId);
            }
        } else {
            LOG.warn("Account with ID {} not found.", accountId);
        }
        return false;
    }
}
