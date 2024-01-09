package com.rahim.userservice.service.account;

import com.rahim.userservice.ContainerImage;
import com.rahim.userservice.TestDataGenerator;
import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Testcontainers
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class AccountDeletionServiceTest {

    @Autowired
    IAccountDeletionService accountDeletionService;

    @Autowired
    AccountRepository accountRepository;

    @Mock
    IEmailTokenGenerator emailTokenGenerator;

    @Mock
    IProfileQueryService profileQueryService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(ContainerImage.POSTGRES.getDockerImageName());

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(ContainerImage.KAFKA.getDockerImageName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    public static void beforeAll() {
        postgresContainer.start();
        kafkaContainer.start();
        String bootstrapServer = kafkaContainer.getBootstrapServers();
        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServer);

        Flyway flyway = Flyway.configure()
                .dataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword())
                .locations("classpath:tables")
                .load();

        flyway.migrate();
    }

    @BeforeEach
    void setup() {
        final int numOfData = 10;
        accountRepository.deleteAll();
        List<Account> accountList = TestDataGenerator.getInstance().generateAccountData(numOfData);

        for (int i = 0; i < numOfData; i++) {
            accountRepository.save(accountList.get(i));
        }

        MockitoAnnotations.openMocks(this);

        doNothing().when(emailTokenGenerator).generateEmailTokens(anyString(), anyInt(), anyBoolean(), anyBoolean());
        when(profileQueryService.getProfileDetails(anyInt())).thenReturn(generateMockProfileDetails());
    }

    @AfterEach
    void resetIdentityCounter() {
        jdbcTemplate.execute("TRUNCATE TABLE rgts.user_accounts RESTART IDENTITY CASCADE");
    }

    private Map<String, Object> generateMockProfileDetails() {
        Map<String, Object> mockProfileDetails = new HashMap<>();
        mockProfileDetails.put("username", "mockUsername");
        mockProfileDetails.put("first_name", "mockFirstName");
        mockProfileDetails.put("last_name", "mockLastName");
        mockProfileDetails.put("email", "mockEmail@example.com");
        mockProfileDetails.put("delete_date", OffsetDateTime.now());
        mockProfileDetails.put("updated_at", OffsetDateTime.now());

        return mockProfileDetails;
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

        assertEquals(AccountState.PENDING_DELETE.getStatus(), account.getAccountStatus());
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
        assertEquals(AccountState.PENDING_DELETE.getStatus(), account.getAccountStatus());
        assertTrue(account.getAccountLocked());
        assertFalse(account.getNotificationSetting());
        assertNotNull(account.getDeleteDate());
    }

}
