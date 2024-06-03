package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.request.account.AccountUpdateRequest;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateAccount(int accountId, AccountUpdateRequest updateRequest) {
        Account account = accountRepositoryHandler.findById(accountId);
        Account originalAccount = new Account(account);

        updateFields(account, updateRequest);
        if (account.equals(originalAccount)) {
            LOG.debug("No updates were applied to the account");
            return "No updates were applied to the account.";
        }

        accountRepositoryHandler.saveAccount(account);
        generateEmailTokens(accountId, originalAccount.getEmail());
        return account;
    }

    private void generateEmailTokens(int accountId, String oldEmail) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)
                .includeUsername(true)
                .includeDate(true)
                .oldEmail(oldEmail)
                .build();

        emailTokenGenerator.generateEmailTokens(emailProperty);
    }

    private void updateFields(Account account, AccountUpdateRequest updateRequest) {
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
            updateEmail(account, updateRequest.getEmail());
        }
        if (updateRequest.getPasswordHash() != null && !updateRequest.getPasswordHash().isEmpty()) {
            account.setPasswordHash(updateRequest.getPasswordHash());
            LOG.debug("Password updated successfully");
        }
        if (updateRequest.getNotificationSetting() != null && !updateRequest.getNotificationSetting().isEmpty()) {
            updateNotification(account, updateRequest.getNotificationSetting());
        }
    }

    private void updateEmail(Account account, String newEmail) {
        if (!accountRepositoryHandler.existsByEmail(newEmail)) {
            account.setEmail(newEmail);
            LOG.debug("Email updated successfully");
        }
    }

    private void updateNotification(Account account, String value) {
        try {
            Boolean newNotificationSetting = parseNotificationSetting(value);
            if (isValidChange(account.getNotificationSetting(), newNotificationSetting)) {
                account.setNotificationSetting(newNotificationSetting);
                updateNotificationSet(account.getId(), newNotificationSetting);
                LOG.debug("Notification setting updated successfully for account with ID {}: {}", account.getId(), newNotificationSetting);
            } else {
                LOG.debug("Invalid value passed or no change in notificationSetting. Not updating for account with ID {}", account.getId());
            }
        } catch (IllegalArgumentException e) {
            LOG.error("Failed to update notificationSetting for account with ID {}: {}", account.getId(), e.getMessage());
        }
    }

    private boolean parseNotificationSetting(String value) {
        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            throw new ValidationException("Invalid value for notificationSetting: " + value);
        }
    }

    private void updateNotificationSet(Integer id, boolean notificationEnabled) {
        if (notificationEnabled) {
            hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        } else {
            hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        }
    }

    private <T> boolean isValidChange(T oldValue, T newValue) {
        return (newValue != null) && (!newValue.equals(oldValue));
    }
}
