package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.constant.HazelcastConstant;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.service.account.IAccountDeletionService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountDeletionService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final HazelcastConstant hazelcastConstant;
    private final CacheManager hazelcastCacheManager;

    /**
     * @see IAccountDeletionService
     */
    @Override
    public boolean requestAccountDelete(int accountId) {
        Optional<Account> existingAccountOptional = accountRepositoryHandler.findById(accountId);

        if (existingAccountOptional.isPresent()) {
            Account account = existingAccountOptional.get();

            String accountStatus = account.getAccountStatus();
            if (accountStatus.equals(AccountState.ACTIVE)) {
                LocalDate deletionDate = LocalDate.now().plusDays(30);

                account.setAccountStatus(AccountState.PENDING_DELETE);
                account.setAccountLocked(true);
                account.setNotificationSetting(false);
                account.setDeleteDate(deletionDate);
                hazelcastCacheManager.removeFromSet(hazelcastConstant.getAccountIdSet(), account.getId());
                try {
                    accountRepositoryHandler.saveAccount(account);
                    EmailProperty emailProperty = EmailProperty.builder()
                            .accountId(accountId)
                            .templateName(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)
                            .includeUsername(true)
                            .includeDate(true)
                            .build();
                    emailTokenGenerator.generateEmailTokens(emailProperty);
                    return true;
                } catch (DataAccessException e) {
                    LOG.error("Error updating account with ID {} - {}", accountId, e.getMessage());
                    throw new RuntimeException("Failed to update account.", e);
                }
            } else {
                LOG.debug("Account with ID {} is not eligible for deletion", accountId);
            }
        } else {
            LOG.warn("Account with ID {} not found.", accountId);
        }

        return false;
    }

}
