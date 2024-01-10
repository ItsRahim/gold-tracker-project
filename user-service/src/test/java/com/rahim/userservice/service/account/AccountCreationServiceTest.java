package com.rahim.userservice.service.account;

import com.rahim.userservice.AbstractTestConfig;
import com.rahim.userservice.exception.DuplicateAccountException;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.model.Profile;
import com.rahim.userservice.model.UserRequest;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class AccountCreationServiceTest extends AbstractTestConfig {

    @Autowired
    IAccountCreationService accountCreationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Test
    @DisplayName("Save New Account - Successful")
    void saveNewAccount_SuccessfulSave() {
        Account account = new Account("rahim@gmail.com", "Password1!");
        Profile profile = new Profile(account, "username", "John", "Doe", "07123456789", "20 Bond Street");
        UserRequest userRequest = new UserRequest(account, profile);

        accountCreationService.createAccount(userRequest);

        Optional<Account> accountOptional = accountRepository.findById(account.getId());
        assertTrue(accountOptional.isPresent());

        Account retreivedAccount = accountOptional.get();

        assertEquals(retreivedAccount.getEmail(), account.getEmail());
    }

    @Test
    @DisplayName("Fail To Save Duplicate Account - Same Email")
    void failToSaveDuplicateAccount_SameEmail() {
        Account account1 = new Account("duplicate@gmail.com", "Password123!");
        accountRepository.save(account1);

        Account account2 = new Account("duplicate@gmail.com", "AnotherPassword456");
        Profile profile2 = new Profile(account2, "duplicateUsername", "Jane", "Doe", "07123456789", "25 Bond Street");
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        assertThrows(DuplicateAccountException.class, () -> accountCreationService.createAccount(userRequest2));
    }

    @Test
    @DisplayName("Fail To Save Duplicate Account - Same Username")
    void failToSaveDuplicateAccount_SameUsername() {
        Account account1 = new Account("unique@gmail.com", "Password789!");
        Profile profile1 = new Profile(account1, "duplicateUsername", "Alice", "Smith", "07123456789", "30 Bond Street");
        UserRequest userRequest1 = new UserRequest(account1, profile1);
        accountCreationService.createAccount(userRequest1);

        Account account2 = new Account("anotherUnique@gmail.com", "NewPassword012");
        Profile profile2 = new Profile(account2, "duplicateUsername", "Bob", "Johnson", "07123456789", "35 Bond Street");
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        assertThrows(DuplicateAccountException.class, () -> accountCreationService.createAccount(userRequest2));
    }

    @Test
    @DisplayName("Fail To Save Duplicate Account - Same Email and Username")
    void failToSaveDuplicateAccount_SameEmailAndUsername() {
        Account account1 = new Account("unique@gmail.com", "Password789!");
        Profile profile1 = new Profile(account1, "duplicateUsername", "Alice", "Smith", "07123456789", "30 Bond Street");
        UserRequest userRequest1 = new UserRequest(account1, profile1);
        accountCreationService.createAccount(userRequest1);

        Account account2 = new Account("unique@gmail.com", "NewPassword012");
        Profile profile2 = new Profile(account2, "duplicateUsername", "Bob", "Johnson", "07123456789", "35 Bond Street");
        UserRequest userRequest2 = new UserRequest(account2, profile2);

        assertThrows(DuplicateAccountException.class, () -> accountCreationService.createAccount(userRequest2));
    }

}
