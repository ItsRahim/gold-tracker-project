package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.constant.AccountState;
import com.rahim.userservice.entity.Account;
import com.rahim.userservice.model.EmailProperty;
import com.rahim.userservice.request.account.AccountUpdateRequest;
import com.rahim.userservice.service.account.IAccountUpdateService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import com.rahim.userservice.util.EmailTokenGenerator;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.userservice.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdateService {

    private static final Logger log = LoggerFactory.getLogger(AccountUpdateService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final EmailTokenGenerator emailTokenGenerator;
    private final CacheManager hazelcastCacheManager;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object updateAccount(int accountId, AccountUpdateRequest updateRequest) {
        Account account = accountRepositoryHandler.findById(accountId);
        Account originalAccount = new Account(account);

        updateFields(account, updateRequest);

        if (account.equals(originalAccount)) {
            log.debug("No updates were applied to the account");
            return "No updates were applied to the account.";
        }

        accountRepositoryHandler.saveAccount(account);
        generateEmailTokens(accountId, originalAccount.getEmail());
        log.info("Successfully updated account with id: {}", accountId);
        return account;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAccountForDeletion(Account account, LocalDate deletionDate) {
        account.setAccountStatus(AccountState.PENDING_DELETE);
        account.setAccountLocked(true);
        account.setNotificationSetting(false);
        account.setDeleteDate(deletionDate);
        accountRepositoryHandler.saveAccount(account);
        hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, account.getId());
    }

    private void generateEmailTokens(int accountId, String oldEmail) {
        EmailProperty emailProperty = EmailProperty.builder()
                .accountId(accountId)
                .templateName(EmailTemplate.ACCOUNT_UPDATE)
                .includeUsername(true)
                .includeDate(true)
                .oldEmail(oldEmail)
                .build();

        emailTokenGenerator.generateEmailTokens(emailProperty);
    }

    /**
     * Updates the account based on values present in update request
     *
     * @param account       The account object being updated
     * @param updateRequest A {@link AccountUpdateRequest} object containing update information for the account
     */
    private void updateFields(Account account, AccountUpdateRequest updateRequest) {
        String email = updateRequest.getEmail();
        String password = updateRequest.getPassword();
        String notificationSetting = updateRequest.getNotificationSetting();

        if (email != null && !email.isEmpty()) {
            log.debug("Updating email");
            updateEmail(account, updateRequest.getEmail());
        }

        if (password != null && !password.isEmpty()) {
            log.debug("Updating password");
            char[] encryptedPassword = passwordUtil.encryptPassword(password);
            account.setPassword(encryptedPassword);
        }

        if (notificationSetting != null && !notificationSetting.isEmpty()) {
            log.debug("Updating notification setting");
            updateNotification(account, updateRequest.getNotificationSetting());
        }
    }

    private void updateEmail(Account account, String newEmail) {
        if (!accountRepositoryHandler.existsByEmail(newEmail)) {
            account.setEmail(newEmail);
            log.debug("Email updated successfully");
        }
    }

    private void updateNotification(Account account, String value) {
        try {
            Boolean newNotificationSetting = parseNotificationSetting(value);
            if (account.getNotificationSetting() == newNotificationSetting) {
                log.debug("Invalid value passed or no change in notificationSetting. Not updating for account with ID {}", account.getId());
                return;
            }

            account.setNotificationSetting(newNotificationSetting);
            updateNotificationSet(account.getId(), newNotificationSetting);
            log.debug("Notification setting updated successfully for account with ID {}: {}", account.getId(), newNotificationSetting);
        } catch (IllegalArgumentException e) {
            log.error("Failed to update notificationSetting for account with ID {}: {}", account.getId(), e.getMessage());
        }
    }

    private boolean parseNotificationSetting(String value) {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(value);
        } else {
            throw new ValidationException("Invalid value for notificationSetting: " + value);
        }
    }

    /**
     * Adds or removed account id from Hazelcast set depending on the notificationEnabled field value
     *
     * @param id                  The account id
     * @param notificationEnabled A boolean indicating if notifications are enabled or not
     */
    private void updateNotificationSet(Integer id, boolean notificationEnabled) {
        if (notificationEnabled) {
            hazelcastCacheManager.addToSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        } else {
            hazelcastCacheManager.removeFromSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET, id);
        }
    }
}
