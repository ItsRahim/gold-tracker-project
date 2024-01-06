package com.rahim.userservice.repository;

import com.rahim.userservice.model.Account;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false", "spring.cloud.discovery.enabled = false"})
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @BeforeAll
    public static void beforeAll() {
        kafka.start();
        String bootstrapServer = kafka.getBootstrapServers();
        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServer);

        Flyway flyway = Flyway.configure().dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:tables")
                .load();
        flyway.migrate();
    }

    @Test
    @DisplayName("Testing Kafka and PostgreSQL Connection")
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();

        assertThat(kafka.isCreated()).isTrue();
        assertThat(kafka.isRunning()).isTrue();
    }

    @Test
    @DisplayName("SOMSOMS")
    void test() {
        Account account = new Account("jane.doe@gmail.com", "password1");
        accountRepository.save(account);

        assertTrue(accountRepository.existsAccountByEmail("jane.doe@gmail.com"));
    }
}
