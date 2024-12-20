package com.rahim.userservice.config;

import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractTestConfig {

    private static final String POSTGRES_IMAGE = "postgres:latest";
    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:6.2.1");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CacheManager hazelcastCacheManager;

    @MockBean
    HazelcastIntialiser hazelcastIntialiser;

    @MockBean
    KafkaListenerConfig kafkaListenerConfig;

    @ServiceConnection
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE);
    static final KafkaContainer kafkaContainer = new KafkaContainer(KAFKA_IMAGE);

    static {
        postgresContainer.start();
        kafkaContainer.start();

        String bootstrapServer = kafkaContainer.getBootstrapServers();
        System.setProperty("kafka.bootstrap-servers", bootstrapServer);
    }

    @BeforeAll
    static void setup() {
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

        hazelcastCacheManager.clearSet(HazelcastConstant.ACCOUNT_ID_SET);
        hazelcastCacheManager.clearSet(HazelcastConstant.ACCOUNT_ID_NOTIFICATION_SET);
    }

    private void truncateTableWithIdentityRestart(String tableName) {
        String sql = "TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE";
        jdbcTemplate.execute(sql);
    }
}
