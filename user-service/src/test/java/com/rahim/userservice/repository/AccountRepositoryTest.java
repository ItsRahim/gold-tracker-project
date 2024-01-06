package com.rahim.userservice.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.vault.enabled=false"})
@TestPropertySource("classpath:application-test.yaml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(AccountRepositoryTest.class);

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));


    @Autowired
    AccountRepository accountRepository;

    @BeforeAll
    public static void beforeAll() {
        kafka.start();
        String bootstrapServer = kafka.getBootstrapServers();
        LOG.info("Bootstrap Server: {}", bootstrapServer);
        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServer);
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();

        assertThat(kafka.isCreated()).isTrue();
        assertThat(kafka.isRunning()).isTrue();
    }
}
