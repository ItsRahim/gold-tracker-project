package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletionService {

    private static final Logger log = LoggerFactory.getLogger(AccountDeletionService.class);
    private final AccountUpdateService accountUpdateService;
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean requestAccountDelete(int accountId) {
        Account account = accountRepositoryHandler.findById(accountId);

        if (!isAccountEligibleForDeletion(account)) {
            log.debug("Account with ID {} is not eligible for deletion", accountId);
            return false;
        }

        LocalDate deletionDate = LocalDate.now().plusDays(30);
        accountUpdateService.updateAccountForDeletion(account, deletionDate);
        sendAccountDeletionEmail(accountId);
        log.info("Successfully updated account with id: {}", accountId);
        
        return true;
    }

    private boolean isAccountEligibleForDeletion(Account account) {
        return account.getAccountStatus().equals(AccountState.ACTIVE);
    }

    private void sendAccountDeletionEmail(int accountId) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(EmailTemplate.ACCOUNT_DELETION)
                .includeUsername(true)
                .includeDate(true)
                .build();

        emailTokenGenerator.generateEmailTokens(emailProperty);
    }
}
