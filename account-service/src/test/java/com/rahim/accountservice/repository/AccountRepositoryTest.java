package com.rahim.accountservice.repository;

import com.rahim.accountservice.config.AbstractTestConfig;
import com.rahim.accountservice.model.Account;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest extends AbstractTestConfig {

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setupTestData() {
        accountRepository.deleteAll();

        for(int i = 0; i < 10; i++) {
            Account account = new Account("user" + i + "@example.com", "password" + i);
            accountRepository.save(account);
        }
    }

    @Test
    @DisplayName("Save New Account - Successful")
    void saveNewAccount_SuccessfulSave() {
        Account account = new Account("jane.doe@gmail.com", "password1");
        accountRepository.save(account);

        assertTrue(accountRepository.existsAccountByEmail("jane.doe@gmail.com"));
        assertEquals(11, accountRepository.findAll().size());
    }

    @Test
    @DisplayName("Remove Account By ID - Successful Removal")
    void removeAccountById_SuccessfulRemoval() {
        accountRepository.deleteById(6);
        Optional<Account> removedAccount = accountRepository.findById(6);

        assertTrue(removedAccount.isEmpty());
    }

    @Test
    @DisplayName("Save and Retrieve Account")
    void saveAndRetrieveAccount() {
        Account account = new Account("rahim.ahmed@gmail.com", "Password123!");
        accountRepository.save(account);

        Optional<Account> retrievedAccount = accountRepository.findById(account.getId());

        assertTrue(retrievedAccount.isPresent());
        assertEquals(account.getEmail(), retrievedAccount.get().getEmail());
    }

    @Test
    @DisplayName("Find Non-Existent Account By ID")
    void shouldNotFindNonExistentAccount() {
        Optional<Account> optionalAccount = accountRepository.findById(22);

        assertTrue(optionalAccount.isEmpty());
    }

    @Test
    @DisplayName("Update Existing Account")
    void shouldUpdateExistingAccount() {
        Account newAccount = new Account("rahim.ahmed@gmail.com", "Password123!");
        accountRepository.save(newAccount);

        Optional<Account> retrievedAccountOptional = accountRepository.findById(newAccount.getId());
        assertTrue(retrievedAccountOptional.isPresent());

        Account retrievedAccount = retrievedAccountOptional.get();
        retrievedAccount.setEmail("newemail@gmail.com");
        accountRepository.save(retrievedAccount);

        Optional<Account> updatedAccountOptional = accountRepository.findById(retrievedAccount.getId());
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertEquals("newemail@gmail.com", updatedAccount.getEmail());
    }

}
