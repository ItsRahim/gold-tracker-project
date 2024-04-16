package com.rahim.accountservice.service.repository;

import com.rahim.accountservice.AbstractTestConfig;
import com.rahim.accountservice.TestDataGenerator;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 07/03/2024
 */
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryHandlerTest extends AbstractTestConfig {

    @Autowired
    IAccountRepositoryHandler accountRepositoryHandler;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        final int numOfData = 10;
        List<Account> accountList = TestDataGenerator.getInstance().generateAccountData(numOfData);

        for (int i = 0; i < numOfData; i++) {
            accountRepository.save(accountList.get(i));
        }
    }

    @Test
    @DisplayName("Some Name")
    void someTest() {

    }

}
