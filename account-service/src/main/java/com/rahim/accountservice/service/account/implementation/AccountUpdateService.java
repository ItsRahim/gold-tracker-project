package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.exception.EmailTokenException;
import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.request.AccountRequest;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;

    @Override
    public boolean updateAccount(int accountId, Map<String, String> updatedData) {
        Account account = getAccountById(accountId);
        String oldEmail = account.getEmail();

        try {
            OffsetDateTime beforeUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);
            updateFields(account, updatedData);
            accountRepositoryHandler.saveAccount(account);
            OffsetDateTime afterUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);

            if (beforeUpdate.equals(afterUpdate)) {
                LOG.debug("No updates were applied to the account");
                return false;
            }

            generateEmailTokens(accountId, oldEmail);
            return true;

        } catch (RuntimeException e) {
            LOG.error("Error updating account with ID {}: {}", accountId, e.getMessage());
            throw e;
        }
    }

    private Account getAccountById(int accountId) {
        return accountRepositoryHandler.findById(accountId).orElseThrow(() -> {
            LOG.warn("Account with ID {} not found while updating.", accountId);
            return new UserNotFoundException("Account with ID " + accountId + " not found");
        });
    }

    private void generateEmailTokens(int accountId, String oldEmail) {
        try {
            EmailProperty emailProperty = EmailProperty.builder()
                    .accountId(accountId)
                    .templateName(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)
                    .includeUsername(true)
                    .includeDate(true)
                    .oldEmail(oldEmail)
                    .build();
            emailTokenGenerator.generateEmailTokens(emailProperty);
        } catch (EmailTokenException e) {
            LOG.error("Error generating email tokens for account with ID {}", accountId, e);
            throw new RuntimeException("Failed to generate email tokens for account.", e);
        }
    }

    private void updateFields(Account account, Map<String, String> updatedData) {
        updatedData.forEach((key, value) -> {
            switch (key) {
                case AccountRequest.ACCOUNT_EMAIL:
                    updateEmail(account, value);
                    break;
                case AccountRequest.ACCOUNT_PASSWORD_HASH:
                    updatePassword(account, value);
                    break;
                case AccountRequest.ACCOUNT_NOTIFICATION_SETTING:
                    updateNotification(account, value);
                    break;
                default:
                    LOG.warn("Ignoring unknown key '{}' in updated data for account with ID {}", key, account.getId());
            }
        });
    }

    private void updateEmail(Account account, String newEmail) {
        if (isValidChange(account.getEmail(), newEmail) && !accountRepositoryHandler.existsByEmail(newEmail)) {
            account.setEmail(newEmail);
            LOG.debug("Email updated successfully");
        } else {
            LOG.debug("Email address update skipped for account with ID: {}", account.getId());
        }
    }

    private void updatePassword(Account account, String newPassword) {
        if (isValidChange(account.getPasswordHash(), newPassword)) {
            account.setPasswordHash(newPassword);
            LOG.debug("Password updated successfully");
        } else {
            LOG.debug("Password update skipped for account with ID: {}", account.getId());
        }
    }

    private void updateNotification(Account account, String value) {
        try {
            Boolean newNotificationSetting = parseNotificationSetting(value);
            if (isValidChange(account.getNotificationSetting(), newNotificationSetting)) {
                updateNotificationSet(account.getId(), newNotificationSetting);
                account.setNotificationSetting(newNotificationSetting);
                LOG.debug("Notification setting updated successfully for account with ID {}: {}", account.getId(), newNotificationSetting);
            } else {
                LOG.error("Invalid value passed or no change in notificationSetting. Not updating for account with ID {}", account.getId());
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
            throw new IllegalArgumentException("Invalid value for notificationSetting: " + value);
        }
    }

    private void updateNotificationSet(Integer id, boolean notificationEnabled) {
        if (notificationEnabled) {
            hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_SET, id);
        } else {
            hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_SET, id);
        }
    }

    private <T> boolean isValidChange(T oldValue, T newValue) {
        return (newValue != null) && (!newValue.equals(oldValue));
    }
}
