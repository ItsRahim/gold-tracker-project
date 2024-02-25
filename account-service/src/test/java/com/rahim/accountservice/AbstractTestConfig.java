package com.rahim.accountservice;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public abstract class AbstractTestConfig {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @ServiceConnection
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(ContainerImage.POSTGRES.getDockerImageName());

    static final KafkaContainer kafkaContainer = new KafkaContainer(ContainerImage.KAFKA.getDockerImageName());

    static {
        postgresContainer.start();
        kafkaContainer.start();

        String bootstrapServer = kafkaContainer.getBootstrapServers();
        System.setProperty("spring.kafka.bootstrap-servers", bootstrapServer);

    }

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgresContainer.getJdbcUrl(), postgresContainer.getUsername(), postgresContainer.getPassword())
                .locations("classpath:tables")
                .load();

        flyway.migrate();
    }

    @AfterEach
    void resetIdentityCounter() {
        String[] tables = {"rgts.user_accounts", "rgts.user_profiles", "rgts.audit_log", "rgts.archived_users"};

        for (String table : tables) {
            truncateTableWithIdentityRestart(table);
        }
    }

    private void truncateTableWithIdentityRestart(String tableName) {
        String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE";
        jdbcTemplate.execute(sql);
    }

    public Map<String, Object> generateMockProfileDetails() {
        Map<String, Object> mockProfileDetails = new HashMap<>();
        mockProfileDetails.put("username", "mockUsername");
        mockProfileDetails.put("first_name", "mockFirstName");
        mockProfileDetails.put("last_name", "mockLastName");
        mockProfileDetails.put("email", "mockEmail@example.com");
        mockProfileDetails.put("delete_date", OffsetDateTime.now());
        mockProfileDetails.put("updated_at", OffsetDateTime.now());

        return mockProfileDetails;
    }

}
