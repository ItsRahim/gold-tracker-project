package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.json.AccountJson;
import com.rahim.accountservice.service.account.IAccountUpdateService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import com.rahim.accountservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> updateAccount(int accountId, Map<String, String> updatedData) {
        Account account = accountRepositoryHandler.findById(accountId);

        if (account.getId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account was not found");
        }

        try {
            String oldEmail = account.getEmail();
            OffsetDateTime beforeUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);
            updateFields(account, updatedData);
            accountRepositoryHandler.saveAccount(account);
            OffsetDateTime afterUpdate = accountRepositoryHandler.getUpdatedAtByUserId(accountId);

            if (beforeUpdate.equals(afterUpdate)) {
                LOG.debug("No updates were applied to the account");
                return ResponseEntity.status(HttpStatus.OK).body("No updates were applied to the account.");
            }

            generateEmailTokens(accountId, oldEmail);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while updating account: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while updating account");
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
            hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        } else {
            hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        }
    }

    private <T> boolean isValidChange(T oldValue, T newValue) {
        return (newValue != null) && (!newValue.equals(oldValue));
    }
}
