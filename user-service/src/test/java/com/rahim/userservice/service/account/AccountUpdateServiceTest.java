package com.rahim.userservice.service.account;

import com.rahim.userservice.AbstractTestConfig;
import com.rahim.userservice.TestDataGenerator;
import com.rahim.userservice.enums.TemplateNameEnum;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class AccountUpdateServiceTest extends AbstractTestConfig {

    @Autowired
    IAccountUpdateService accountUpdateService;

    @Autowired
    AccountRepository accountRepository;

    @Mock
    IEmailTokenGenerator emailTokenGenerator;

    @Mock
    IProfileQueryService profileQueryService;

    @BeforeEach
    void setup() {
        final int numOfData = 10;
        List<Account> accountList = TestDataGenerator.getInstance().generateAccountData(numOfData);

        for (int i = 0; i < numOfData; i++) {
            accountRepository.save(accountList.get(i));
        }

        MockitoAnnotations.openMocks(this);

        doNothing().when(emailTokenGenerator).generateEmailTokens(anyString(), anyInt(), anyBoolean(), anyBoolean());
        when(profileQueryService.getProfileDetails(anyInt())).thenReturn(generateMockProfileDetails());
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
                .generateEmailTokens(eq(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName()), eq(accountId), eq(true), eq(true), eq(oldEmail));

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
                .generateEmailTokens(eq(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName()), eq(accountId), eq(true), eq(true), eq(oldEmail));

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
                .generateEmailTokens(eq(TemplateNameEnum.ACCOUNT_UPDATE.getTemplateName()), eq(accountId), eq(true), eq(true), eq(email));

        assertNotEquals(oldPassword, updatedAccount.getPasswordHash());
    }

    @Test
    @DisplayName("Account Not Found - UserNotFoundException")
    void accountNotFound_UserNotFoundException() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Invalid Email Update (Email Already Exists) - Warning Log")
    void invalidEmailUpdate_EmailAlreadyExists_WarningLog() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Invalid Password Update (Empty Password) - Warning Log")
    void invalidPasswordUpdate_EmptyPassword_WarningLog() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Invalid Password Update (No Password Provided) - Warning Log")
    void invalidPasswordUpdate_NoPasswordProvided_WarningLog() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Error During Account Update - RuntimeException")
    void errorDuringAccountUpdate_RuntimeException() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Valid Email Update with Same Password - Successful")
    void validEmailUpdateWithSamePassword_Successful() {
        // Implement the test case based on the provided scenario
    }

    @Test
    @DisplayName("Valid Password Update with Same Email - Successful")
    void validPasswordUpdateWithSameEmail_Successful() {
        // Implement the test case based on the provided scenario
    }

}
