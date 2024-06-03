package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.json.AccountJson;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateAccount(int accountId, Map<String, String> updatedData) {
        Account account = accountRepositoryHandler.findById(accountId);

        try {
            String oldEmail = account.getEmail();
            Account originalAccount = new Account(account);

            updateFields(account, updatedData);
            accountRepositoryHandler.saveAccount(account);

            if (account.equals(originalAccount)) {
                LOG.debug("No updates were applied to the account");
                return "No updates were applied to the account.";
            }

            generateEmailTokens(accountId, oldEmail);
            return account;
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while updating account: {}", e.getMessage(), e);
            throw new DatabaseException("An unexpected error occurred while updating account");
        }
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

    private void updateFields(Account account, Map<String, String> updatedData) {
        updatedData.forEach((key, value) -> {
            switch (key) {
                case AccountJson.ACCOUNT_EMAIL:
                    if (isValidChange(account.getEmail(), value)) {
                        updateEmail(account, value);
                    }
                    break;
                case AccountJson.ACCOUNT_PASSWORD_HASH:
                    if (isValidChange(account.getPasswordHash(), value)) {
                        updatePassword(account, value);
                    }
                    break;
                case AccountJson.ACCOUNT_NOTIFICATION_SETTING:
                    if (isValidChange(account.getNotificationSetting(), value)) {
                        updateNotification(account, value);
                    }
                    break;
                default:
                    LOG.warn("Ignoring unknown key '{}' in updated data for account with ID {}", key, account.getId());
            }
        });
    }

    private void updateEmail(Account account, String newEmail) {
        if (!accountRepositoryHandler.existsByEmail(newEmail)) {
            account.setEmail(newEmail);
            LOG.debug("Email updated successfully");
        }
    }

    private void updatePassword(Account account, String newPassword) {
        account.setPasswordHash(newPassword);
        LOG.debug("Password updated successfully");
    }

    private void updateNotification(Account account, String value) {
        try {
            Boolean newNotificationSetting = parseNotificationSetting(value);
            if (!isValidChange(account.getNotificationSetting(), newNotificationSetting)) {
                LOG.debug("Invalid value passed or no change in notificationSetting. Not updating for account with ID {}", account.getId());
                return;
            }

            updateNotificationSet(account.getId(), newNotificationSetting);
            account.setNotificationSetting(newNotificationSetting);
            LOG.debug("Notification setting updated successfully for account with ID {}: {}", account.getId(), newNotificationSetting);
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
