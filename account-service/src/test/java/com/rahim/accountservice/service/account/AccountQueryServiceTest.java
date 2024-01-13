package com.rahim.accountservice.service.account;

import com.rahim.accountservice.AbstractTestConfig;
import com.rahim.accountservice.TestDataGenerator;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class AccountQueryServiceTest extends AbstractTestConfig {

    @Autowired
    IAccountQueryService accountQueryService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Mock
    IKafkaService kafkaService;

    @Captor
    ArgumentCaptor<String> topicCaptor;

    @Captor
    ArgumentCaptor<String> messageCaptor;

    @Value("${topics.send-id-result}")
    String sendIdResult;

    @BeforeEach
    public void setUp() {
        final int numOfData = 10;
        List<Account> accountList = TestDataGenerator.getInstance().generateAccountData(numOfData);

        for (int i = 0; i < numOfData; i++) {
            accountRepository.save(accountList.get(i));
        }

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(accountQueryService, "kafkaService", kafkaService);
    }

    @Test
    @DisplayName("Exists By Id - User Found")
    public void existsById_UserFound() {
        String accountId = "1";
        accountQueryService.existsById(accountId);

        verify(kafkaService).sendMessage(topicCaptor.capture(), messageCaptor.capture());

        assertEquals(sendIdResult, topicCaptor.getValue());
        assertEquals("true", messageCaptor.getValue());
    }

    @Test
    @DisplayName("Exists By Id - User Not Found")
    public void existsById_UserNotFound() {
        String accountId = "10000";
        accountQueryService.existsById(accountId);

        verify(kafkaService).sendMessage(topicCaptor.capture(), messageCaptor.capture());

        assertEquals(sendIdResult, topicCaptor.getValue());
        assertEquals("false", messageCaptor.getValue());
    }

    @Test
    @DisplayName("Get All Accounts - Accounts Found")
    public void getAllAccounts_AccountsFound() {
        List<Account> accountList = accountQueryService.getAllAccounts();

        assertFalse(accountList.isEmpty());
        assertEquals(accountList.size(), 10);

    }

    @Test
    @DisplayName("Get All Accounts - No Accounts Found")
    public void getAllAccounts_NoAccountsFound() {
        String sql = "TRUNCATE TABLE rgts.user_accounts RESTART IDENTITY CASCADE";
        jdbcTemplate.execute(sql);

        List<Account> accountList = accountQueryService.getAllAccounts();

        assertTrue(accountList.isEmpty());
    }

}
