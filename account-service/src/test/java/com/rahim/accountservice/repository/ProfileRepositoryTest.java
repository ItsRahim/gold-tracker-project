package com.rahim.accountservice.repository;

import com.rahim.accountservice.AbstractTestConfig;
import com.rahim.accountservice.TestDataGenerator;
import com.rahim.accountservice.model.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@ActiveProfiles("test")
@TestPropertySource("classpath:application.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class ProfileRepositoryTest extends AbstractTestConfig {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setupTestData() {
        final int numOfData = 10;
        List<UserRequest> userRequests = TestDataGenerator.getInstance().generateUserRequestData(numOfData);

        for(UserRequest userRequest : userRequests) {
            accountRepository.save(userRequest.getAccount());
            profileRepository.save(userRequest.getProfile());
        }
    }

    @Test
    @DisplayName("Some name")
    void sometest_test() {
        System.out.println("Do something");
    }
}
