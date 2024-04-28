package com.rahim.accountservice.service.account;

import com.rahim.accountservice.AbstractTestConfig;
import com.rahim.accountservice.TestDataGenerator;
import com.rahim.accountservice.constant.AccountState;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountDeletionServiceTest extends AbstractTestConfig {

    @Autowired
    IAccountDeletionService accountDeletionService;

    @Autowired
    AccountRepository accountRepository;

    @Mock
    IEmailTokenGenerator emailTokenGenerator;

    @Mock
    IProfileRepositoryHandler profileRepositoryHandler;

    @BeforeEach
    void setup() {
        final int numOfData = 10;
        List<Account> accountList = TestDataGenerator.getInstance().generateAccountData(numOfData);

        for (int i = 0; i < numOfData; i++) {
            accountRepository.save(accountList.get(i));
        }

        MockitoAnnotations.openMocks(this);

        doNothing().when(emailTokenGenerator).generateEmailTokens(anyString(), anyInt(), anyBoolean(), anyBoolean());
        when(profileRepositoryHandler.getProfileDetails(anyInt())).thenReturn(generateMockProfileDetails());
    }

    @Test
    @DisplayName("Request Account Deletion - Successful")
    void requestAccountDelete_Successful() {
        int accountId = 1;

        boolean result = accountDeletionService.requestAccountDelete(accountId);
        assertTrue(result);

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account account = accountOptional.get();

        assertEquals(AccountState.PENDING_DELETE, account.getAccountStatus());
        assertTrue(account.getAccountLocked());
        assertNotNull(account.getDeleteDate());
        Mockito.verify(emailTokenGenerator, Mockito.times(0))
                .generateEmailTokens(anyString(), anyInt(), anyBoolean(), anyBoolean());
    }

    @Test
    @DisplayName("Request Account Deletion - Invalid Account ID")
    void requestAccountDelete_InvalidId() {
        int accountId = 100;

        boolean result = accountDeletionService.requestAccountDelete(accountId);
        assertFalse(result);
    }

    @Test
    @DisplayName("Request Account Deletion - Already Pending Deletion")
    void requestAccountDelete_AlreadyPendingDeletion() {
        int accountId = 5;

        // Requesting to Delete Account
        assertTrue(accountDeletionService.requestAccountDelete(accountId));

        // Re-requesting to delete same account
        assertFalse(accountDeletionService.requestAccountDelete(accountId));
        verifyNoInteractions(emailTokenGenerator);

        Account account = accountRepository.findById(accountId).orElseThrow();
        assertEquals(AccountState.PENDING_DELETE, account.getAccountStatus());
        assertTrue(account.getAccountLocked());
        assertFalse(account.getNotificationSetting());
        assertNotNull(account.getDeleteDate());
    }

}
