package com.rahim.userservice.service.account;

import com.hazelcast.collection.ISet;
import com.rahim.userservice.config.AbstractTestConfig;
import com.rahim.userservice.config.TestDataGenerator;
import com.rahim.userservice.entity.Account;
import com.rahim.userservice.model.Address;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.request.account.AccountCreationRequest;
import com.rahim.userservice.request.account.AccountUpdateRequest;
import com.rahim.userservice.request.profile.ProfileCreationRequest;
import com.rahim.userservice.service.repository.implementation.AccountRepositoryHandlerService;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.userservice.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Author: Rahim Ahmed
 * Created: 18/06/2024
 */
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountUpdateSvcTest extends AbstractTestConfig {

    @Autowired
    private IAccountUpdateService accountUpdateService;

    @Autowired
    private IAccountCreationService accountCreationService;

    @Autowired
    private AccountRepositoryHandlerService accountRepositoryHandler;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private CacheManager hazelcastCacheManager;

    private static final Address address = TestDataGenerator.generateAddress();
    private UserRequest userRequest;

    @BeforeEach
    void generateTestData() {
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest("metus@icloud.org", "metus123!");
        ProfileCreationRequest profileCreationRequest = new ProfileCreationRequest("metus", "Stephen", "Drake", "07678584701", address);
        UserRequest initialCreationRequest = new UserRequest(accountCreationRequest, profileCreationRequest);
        userRequest = accountCreationService.createAccount(initialCreationRequest);

        assertThat(userRequest).isNotNull();
    }

    private Integer getAccountIdForUserRequest(UserRequest userRequest) {
        return accountRepositoryHandler.getAllAccounts()
                .stream()
                .filter(account -> account.getEmail().equals(userRequest.getAccount().getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + userRequest.getAccount().getEmail()))
                .getId();
    }

    @Test
    @DisplayName("Successful account update")
    void shouldUpdateAccountSuccessfully() {
        ISet<Integer> accountIdNotifs = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIdNotifs).isEmpty();

        Integer accountId = getAccountIdForUserRequest(userRequest);

        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest("new.email@gmail.com", "newPassword123!", "true");
        Object updatedObject = accountUpdateService.updateAccount(accountId, accountUpdateRequest);

        accountIdNotifs = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIdNotifs).isNotEmpty();

        assertThat(updatedObject).isInstanceOf(Account.class);
        Account updatedAccount = (Account) updatedObject;

        String updatedEmail = accountUpdateRequest.getEmail();
        String updatedPassword = accountUpdateRequest.getPassword();
        Boolean updatedNotification = Boolean.parseBoolean(accountUpdateRequest.getNotificationSetting());

        assertThat(updatedAccount.getId()).isEqualTo(accountId);
        assertThat(updatedAccount.getEmail()).isEqualTo(updatedEmail);

        boolean passwordCheck = passwordUtil.checkPassword(updatedPassword, updatedAccount.getPassword());
        assertThat(passwordCheck).isTrue();
        assertThat(updatedAccount.getNotificationSetting()).isEqualTo(updatedNotification);
    }

    @Test
    @DisplayName("No Updates Applied")
    void shouldNotUpdateAccount_NoChanges() {
        assertThat(userRequest).isNotNull();

        Integer accountId = getAccountIdForUserRequest(userRequest);

        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest(null, null, "false");
        Object object = accountUpdateService.updateAccount(accountId, accountUpdateRequest);
        assertThat(object).isInstanceOf(String.class);

        String response = (String) object;
        assertThat(response).isEqualTo("No updates were applied to the account.");
    }

    @Test
    @DisplayName("Email Already Exists")
    void shouldNotUpdateAccount_DuplicateEmail() {
        assertThat(userRequest).isNotNull();

        Integer accountId = getAccountIdForUserRequest(userRequest);

        AccountCreationRequest accountCreationRequest = new AccountCreationRequest("rahim@gmail.com", "Password123!");
        ProfileCreationRequest profileCreationRequest = new ProfileCreationRequest("rahim.ahmed", "Rahim", "Ahmed", "07987676653", address);
        accountCreationService.createAccount(new UserRequest(accountCreationRequest, profileCreationRequest));

        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest("rahim@gmail.com", null, null);
        Object object = accountUpdateService.updateAccount(accountId, accountUpdateRequest);
        assertThat(object).isInstanceOf(String.class);

        String response = (String) object;
        assertThat(response).isEqualTo("No updates were applied to the account.");
    }

    @Test
    @DisplayName("Invalid Notification Setting Value")
    void shouldThrowException_InvalidNotificationSettingValue() {
        assertThat(userRequest).isNotNull();

        Integer accountId = getAccountIdForUserRequest(userRequest);

        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest(null, null, "INVALID_VALUE");
        assertThrows(ValidationException.class, () -> accountUpdateService.updateAccount(accountId, accountUpdateRequest));

        Account account = accountRepositoryHandler.findById(accountId);
        boolean notificationSetting = account.getNotificationSetting();
        assertThat(notificationSetting).isEqualTo(false);
    }

    @Test
    @DisplayName("Invalid Account ID")
    void shouldHandleInvalidAccountID() {
        assertThat(userRequest).isNotNull();

        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest("rahim@gmail.com", null, null);
        assertThrows(EntityNotFoundException.class, () -> accountUpdateService.updateAccount(10000, accountUpdateRequest));
    }
}
