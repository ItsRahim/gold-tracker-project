package com.rahim.userservice.service.account;

import com.hazelcast.collection.ISet;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.userservice.config.AbstractTestConfig;
import com.rahim.userservice.config.TestDataGenerator;
import com.rahim.userservice.constant.AccountState;
import com.rahim.userservice.entity.Account;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.service.account.implementation.AccountCreationService;
import com.rahim.userservice.service.account.implementation.AccountDeletionService;
import com.rahim.userservice.service.repository.IAccountRepositoryHandler;
import com.rahim.userservice.util.EmailTokenGenerator;
import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;

/**
 * @author Rahim Ahmed
 * @created 20/06/2024
 */

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountDeletionSvcTest extends AbstractTestConfig {

    @Autowired
    private AccountDeletionService accountDeletionService;

    @Autowired
    private AccountCreationService accountCreationService;

    @Autowired
    private IAccountRepositoryHandler accountRepositoryHandler;

    @Autowired
    private CacheManager hazelcastCacheManager;

    @Autowired
    private EmailTokenGenerator emailTokenGenerator;

    private Integer accountId;

    private Integer getAccountIdForUserRequest(UserRequest userRequest) {
        return accountRepositoryHandler.getAllAccounts()
                .stream()
                .filter(account -> account.getEmail().equals(userRequest.getAccount().getEmail()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account not found for email: " + userRequest.getAccount().getEmail()))
                .getId();
    }

    @BeforeEach
    void generateTestData() {
        UserRequest userRequestData = TestDataGenerator.generateSingleUserRequest();
        accountCreationService.createAccount(userRequestData);
        accountId = getAccountIdForUserRequest(userRequestData);
    }

    @Test
    @DisplayName("Successful Account Deletion Request")
    void deleteAccount_Success() {
        boolean result = accountDeletionService.requestAccountDelete(accountId);
        assertThat(result).isTrue();

        Account deletedAccount = accountRepositoryHandler.findById(accountId);
        assertThat(deletedAccount.getAccountStatus()).isEqualTo(AccountState.PENDING_DELETE);
        assertThat(deletedAccount.getAccountLocked()).isTrue();
        assertThat(deletedAccount.getNotificationSetting()).isFalse();
        assertThat(deletedAccount.getDeleteDate()).isNotNull();

        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIds.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Unsuccessful Delete - Account Pending Delete")
    void deleteAccount_UnsuccessfulPendingDelete() {
        Account priginalAccount = accountRepositoryHandler.findById(accountId);
        priginalAccount.setAccountStatus(AccountState.PENDING_DELETE);
        accountRepositoryHandler.saveAccount(priginalAccount);

        boolean result = accountDeletionService.requestAccountDelete(accountId);
        assertThat(result).isFalse();

        Account unchangedAccount = accountRepositoryHandler.findById(accountId);
        assertThat(unchangedAccount.getAccountStatus()).isEqualTo(AccountState.PENDING_DELETE);
        assertThat(unchangedAccount).isEqualTo(priginalAccount);

        ISet<Integer> accountIds = hazelcastCacheManager.getSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
        assertThat(accountIds.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Unsuccessful Delete - Invalid Account Id")
    void deleteAccount_InvalidAccountId() {
        assertThrows(EntityNotFoundException.class, ()-> accountDeletionService.requestAccountDelete(1000));
    }
}
