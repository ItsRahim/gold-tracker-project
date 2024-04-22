package com.rahim.accountservice.service.account;

import com.rahim.accountservice.AbstractTestConfig;
import com.rahim.accountservice.TestDataGenerator;
import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rahim.accountservice.constant.EmailTemplates.ACCOUNT_UPDATE_TEMPLATE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountUpdateServiceTest extends AbstractTestConfig {

    @Autowired
    IAccountUpdateService accountUpdateService;

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
    @DisplayName("Update Account Email & Password - Successful")
    void updateAccount_SuccessfulUpdate() throws Exception {
        int accountId = 1;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account oldAccount = accountOptional.get();
        String oldEmail = oldAccount.getEmail();
        String oldPassword = oldAccount.getPasswordHash();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "new.email@example.com");
        updatedData.put("passwordHash", "newPasswordHash");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertEquals(updatedData.get("email"), updatedAccount.getEmail());
        assertEquals(updatedData.get("passwordHash"), updatedAccount.getPasswordHash());

        verify(emailTokenGenerator, times(0))
                .generateEmailTokens(ACCOUNT_UPDATE_TEMPLATE, accountId, true, true, oldEmail);

        assertFalse(accountRepository.existsAccountByEmail(oldEmail));
        assertNotEquals(updatedAccount.getPasswordHash(), oldPassword);
    }

    @Test
    @DisplayName("Update Account Email Only - Successful")
    void updateAccountEmail_SuccessfulUpdate() throws Exception {
        int accountId = 5;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account oldAccount = accountOptional.get();
        String oldEmail = oldAccount.getEmail();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "new.email@example.com");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertEquals(updatedData.get("email"), updatedAccount.getEmail());

        verify(emailTokenGenerator, times(0))
                .generateEmailTokens(eq(ACCOUNT_UPDATE_TEMPLATE), eq(accountId), eq(true), eq(true), eq(oldEmail));

        assertFalse(accountRepository.existsAccountByEmail(oldEmail));
        assertTrue(accountRepository.existsAccountByEmail(updatedAccount.getEmail()));
    }

    @Test
    @DisplayName("Update Password Only - Successful")
    void updatePasswordOnly_Successful() throws Exception {
        int accountId = 5;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account oldAccount = accountOptional.get();
        String email = oldAccount.getEmail();
        String oldPassword = oldAccount.getPasswordHash();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("passwordHash", "NewPassword123!");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertEquals(updatedData.get("passwordHash"), updatedAccount.getPasswordHash());

        verify(emailTokenGenerator, times(0))
                .generateEmailTokens(eq(ACCOUNT_UPDATE_TEMPLATE), eq(accountId), eq(true), eq(true), eq(email));

        assertNotEquals(oldPassword, updatedAccount.getPasswordHash());
    }

    @Test
    @DisplayName("Account Not Found - UserNotFoundException")
    void accountNotFound_UserNotFoundException() {
        int accountId = 1011;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertFalse(accountOptional.isPresent());

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "SomeNewMail@mail.com!");
        updatedData.put("passwordHash", "NewPassword123!");

        assertThrows(UserNotFoundException.class, () -> accountUpdateService.updateAccount(accountId, updatedData));

    }

    @Test
    @DisplayName("Invalid Email Update (Email Already Exists) - Warning Log")
    void invalidEmailUpdate_EmailAlreadyExists() throws Exception {
        Account existingAccount = new Account("test@gmail.com", "password1!");
        Account account = new Account("test1@gmail.com", "password1!");
        accountRepository.saveAll(List.of(existingAccount, account));

        int accountId = account.getId();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "test@gmail.com");
        updatedData.put("passwordHash", "NewPassword123!");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();

        assertNotEquals(account.getUpdatedAt(), updatedAccount.getUpdatedAt());
        assertNotEquals(account.getPasswordHash(), updatedAccount.getPasswordHash());
        assertEquals(account.getEmail(), updatedAccount.getEmail());
    }

    @Test
    @DisplayName("Invalid Email Update (Empty Email)")
    void invalidPasswordUpdate_EmptyEmail() throws Exception {
        int accountId = 5;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account oldAccount = accountOptional.get();
        String oldEmail = oldAccount.getEmail();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertNotEquals(updatedData.get("email"), updatedAccount.getEmail());

        assertEquals(oldEmail, updatedAccount.getEmail());
        assertEquals(oldAccount.getUpdatedAt(), updatedAccount.getUpdatedAt());
    }

    @Test
    @DisplayName("Invalid Password Update (Empty Password)")
    void invalidPasswordUpdate_EmptyPassword() throws Exception {
        int accountId = 5;
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        assertTrue(accountOptional.isPresent());

        Account oldAccount = accountOptional.get();
        String oldPassword = oldAccount.getPasswordHash();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("passwordHash", "");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertNotEquals(updatedData.get("passwordHash"), updatedAccount.getPasswordHash());

        assertEquals(oldPassword, updatedAccount.getPasswordHash());
        assertEquals(oldAccount.getUpdatedAt(), updatedAccount.getUpdatedAt());
    }

    @Test
    @DisplayName("Valid Email Update with Same Password - Successful")
    void validEmailUpdateWithSamePassword_Successful() throws Exception {
        Account account = new Account("test@gmail.com", "password1!");
        accountRepository.save(account);

        int accountId = account.getId();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "someNewEmail@gmail.com");
        updatedData.put("passwordHash", "password1!");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();

        assertEquals(updatedData.get("email"), updatedAccount.getEmail());

        assertEquals(account.getPasswordHash(), updatedAccount.getPasswordHash());

        assertFalse(accountRepository.existsAccountByEmail(account.getEmail()));
        assertTrue(accountRepository.existsAccountByEmail(updatedData.get("email")));

    }

    @Test
    @DisplayName("Valid Password Update with Same Email - Successful")
    void validPasswordUpdateWithSameEmail_Successful() throws Exception {
        Account account = new Account("test@gmail.com", "password1!");
        accountRepository.save(account);

        int accountId = account.getId();

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("email", "test@gmail.com");
        updatedData.put("passwordHash", "someNewPassword123!");

        accountUpdateService.updateAccount(accountId, updatedData);

        Optional<Account> updatedAccountOptional = accountRepository.findById(accountId);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();

        assertEquals(updatedData.get("passwordHash"), updatedAccount.getPasswordHash());

        assertEquals(account.getEmail(), updatedAccount.getEmail());

    }

}
