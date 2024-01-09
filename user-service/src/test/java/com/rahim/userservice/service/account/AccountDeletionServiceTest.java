package com.rahim.userservice.service.account;

import com.rahim.userservice.ContainerImage;
import com.rahim.userservice.TestDataGenerator;
import com.rahim.userservice.enums.AccountState;
import com.rahim.userservice.model.Account;
import com.rahim.userservice.repository.AccountRepository;
import com.rahim.userservice.service.profile.IProfileQueryService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import org.flywaydb.core.Flyway;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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

        Account newData = accountRepository.findById(accountId).get();

        assertEquals(AccountState.PENDING_DELETE.getStatus(), newData.getAccountStatus());
        assertTrue(newData.getAccountLocked());
        assertNotNull(newData.getDeleteDate());
    }
}
