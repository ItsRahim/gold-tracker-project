package com.rahim.accountservice.service.account;

import com.hazelcast.collection.ISet;
import com.rahim.accountservice.config.AbstractTestConfig;
import com.rahim.accountservice.config.TestDataGenerator;
import com.rahim.accountservice.entity.Account;
import com.rahim.accountservice.model.Address;
import com.rahim.accountservice.model.UserRequest;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.request.account.AccountCreationRequest;
import com.rahim.accountservice.request.account.AccountUpdateRequest;
import com.rahim.accountservice.request.profile.ProfileCreationRequest;
import com.rahim.accountservice.service.repository.implementation.AccountRepositoryHandlerService;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
    private AccountRepository accountRepository;

    @Autowired
    private CacheManager hazelcastCacheManager;

    UserRequest generateData() {
        Address address = TestDataGenerator.generateAddress();
        AccountCreationRequest accountCreationRequest = new AccountCreationRequest("metus@icloud.org", "metus123!");
        ProfileCreationRequest profileCreationRequest = new ProfileCreationRequest("metus", "Stephen", "Drake", "07678584701", address);

        return new UserRequest(accountCreationRequest, profileCreationRequest);
    }

    @Test
    @DisplayName("Successful account update")
    void shouldUpdateAccountSuccessfully() {
        // Creating initial account
        UserRequest userRequest = accountCreationService.createAccount(generateData());

        // Verify account id is not present in hazelcast account notification set
        ISet<Integer> accountIdNotifs = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIdNotifs).isNullOrEmpty();

        // Verifying new account has been created
        assertThat(userRequest).isNotNull();

        // Getting original account
        Account originalAccount = accountRepositoryHandler.getAllAccounts()
                .stream()
                .filter(account -> account.getEmail().equals(userRequest.getAccount().getEmail()))
                .findFirst()
                .get();

        assertThat(originalAccount).isNotNull();
        Integer accountId = originalAccount.getId();

        // Creating update account request data
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest("new.email@gmail.com", "newPassword123!", "true");

        // Performing account update
        Object updatedObject = accountUpdateService.updateAccount(accountId, accountUpdateRequest);

        // Asserting account id has been added to hazelcast notification set
        accountIdNotifs = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIdNotifs).isNotEmpty();

        // Asserting that the update was successful and returned an Account object
        assertThat(updatedObject).isInstanceOf(Account.class);
        Account updatedAccount = (Account) updatedObject;

        String updatedEmail = accountUpdateRequest.getEmail();
        String updatedPassword = accountUpdateRequest.getPasswordHash();
        Boolean updatedNotification = Boolean.parseBoolean(accountUpdateRequest.getNotificationSetting());

        // Verifying the updated account details
        assertThat(updatedAccount.getId()).isEqualTo(accountId);
        assertThat(updatedAccount.getEmail()).isEqualTo(updatedEmail);
        assertThat(updatedAccount.getPasswordHash()).isEqualTo(updatedPassword);
        assertThat(updatedAccount.getNotificationSetting()).isEqualTo(updatedNotification);

        // Reload the account from the repository to verify persistence
        Optional<Account> reloadedAccountOptional = accountRepository.findById(accountId);
        assertThat(reloadedAccountOptional).isPresent();
        Account reloadedAccount = reloadedAccountOptional.get();

        // Asserting that reloaded account matches the updated account
        assertThat(reloadedAccount.getEmail()).isEqualTo(updatedEmail);
        assertThat(reloadedAccount.getPasswordHash()).isEqualTo(updatedPassword);
        assertThat(reloadedAccount.getNotificationSetting()).isEqualTo(updatedNotification);
    }


    @Test
    @DisplayName("No Updates Applied")
    void shouldNotUpdateAccount_NoChanges() {
        // Creating initial account
        UserRequest userRequest = accountCreationService.createAccount(generateData());

        // Verifying new account has been created
        assertThat(userRequest).isNotNull();

        // Creating update account request data with only notification setting (default false)
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest(null, null, "false");

        Integer accountId = accountRepositoryHandler.getAllAccounts()
                .stream()
                .filter(account -> account.getEmail().equals(userRequest.getAccount().getEmail()))
                .findFirst()
                .get()
                .getId();

        assertThat(accountId).isNotNull();

        // Updating account
        Object object = accountUpdateService.updateAccount(accountId, accountUpdateRequest);
        assertThat(object).isInstanceOf(String.class);

        String response = (String) object;
        assertThat(response).isEqualTo("No Updates applied");
    }

    @Test
    @DisplayName("Email Already Exists")
    void shouldNotUpdateAccount_DuplicateEmail() {
        // Test implementation
    }

    @Test
    @DisplayName("Invalid Notification Setting Value")
    void shouldThrowException_InvalidNotificationSettingValue() {
        // Test implementation
    }

    @Test
    @DisplayName("Update Email Only")
    void shouldUpdateEmailOnly() {
        // Test implementation
    }

    @Test
    @DisplayName("Update Password Only")
    void shouldUpdatePasswordOnly() {
        // Test implementation
    }

    @Test
    @DisplayName("Update Notification Setting Only")
    void shouldUpdateNotificationSettingOnly() {
        // Test implementation
    }

    @Test
    @DisplayName("Deleting Account with Pending Delete Status")
    void shouldUpdateAccountForDeletion() {
        // Test implementation
    }

    @Test
    @DisplayName("Invalid Account ID")
    void shouldHandleInvalidAccountID() {
        // Test implementation
    }

    @Test
    @DisplayName("Partial Updates")
    void shouldUpdatePartialFields() {
        // Test implementation
    }
}
