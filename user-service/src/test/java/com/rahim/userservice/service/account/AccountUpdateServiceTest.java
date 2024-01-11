package com.rahim.userservice.service.account;

import com.rahim.userservice.AbstractTestConfig;
import com.rahim.userservice.TestDataGenerator;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
}
