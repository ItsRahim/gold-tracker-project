package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletionService {

    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean requestAccountDelete(int accountId) {
        Account account = accountRepositoryHandler.findById(accountId);

        if (!isAccountEligibleForDeletion(account)) {
            log.debug("Account with ID {} is not eligible for deletion", accountId);
            return false;
        }

        LocalDate deletionDate = LocalDate.now().plusDays(30);
        updateAccountForDeletion(account, deletionDate);
        sendAccountDeletionEmail(accountId);

        return true;
    }

    private boolean isAccountEligibleForDeletion(Account account) {
        return account.getAccountStatus().equals(AccountState.ACTIVE);
    }

    private void updateAccountForDeletion(Account account, LocalDate deletionDate) {
        account.setAccountStatus(AccountState.PENDING_DELETE);
        account.setAccountLocked(true);
        account.setNotificationSetting(false);
        account.setDeleteDate(deletionDate);
        accountRepositoryHandler.saveAccount(account);
        hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, account.getId());
    }

    private void sendAccountDeletionEmail(int accountId) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)
                .includeUsername(true)
                .includeDate(true)
                .build();

        emailTokenGenerator.generateEmailTokens(emailProperty);
    }
}
