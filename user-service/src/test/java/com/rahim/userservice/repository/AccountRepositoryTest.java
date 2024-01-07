package com.rahim.userservice.repository;

import com.rahim.userservice.model.Account;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled=false"})
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16.0");

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @BeforeAll
    public static void beforeAll() {
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
    void setupTestData() {
        accountRepository.deleteAll();

        for(int i = 0; i < 10; i++) {
            Account account = new Account("user" + i + "@example.com", "password" + i);
            accountRepository.save(account);
        }
    }

    @Test
    @DisplayName("Testing PostgreSQL Connection")
    void databaseConnectionEstablished() {
        assertTrue(postgresContainer.isCreated());
        assertTrue(postgresContainer.isRunning());
    }

    @Test
    @DisplayName("Testing Kafka Connection")
    void kafkaConnectionEstablished() {
        assertTrue(kafkaContainer.isCreated());
        assertTrue(kafkaContainer.isRunning());
    }

    @Test
    @DisplayName("Save New Account - Successful")
    void saveNewAccount_SuccessfulSave() {
        Account account = new Account("jane.doe@gmail.com", "password1");
        accountRepository.save(account);

        assertTrue(accountRepository.existsAccountByEmail("jane.doe@gmail.com"));
        assertEquals(accountRepository.findAll().size(), 11);
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
        Optional<Account> accountOptional = accountRepository.findById(3);
        assertTrue(accountOptional.isPresent());

        Account account = accountOptional.get();

        account.setEmail("newemail@gmail.com");
        accountRepository.save(account);

        Optional<Account> updatedAccountOptional = accountRepository.findById(3);
        assertTrue(updatedAccountOptional.isPresent());

        Account updatedAccount = updatedAccountOptional.get();
        assertEquals("newemail@gmail.com", updatedAccount.getEmail());
    }

}
