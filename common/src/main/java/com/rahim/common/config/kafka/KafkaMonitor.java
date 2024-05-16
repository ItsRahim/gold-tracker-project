package com.rahim.common.config.kafka;

import com.rahim.common.config.health.HealthStatus;
import com.rahim.common.dao.KafkaDataAccess;
import com.rahim.common.model.KafkaUnsentMessage;
import com.rahim.common.service.kafka.IKafkaService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author Rahim Ahmed
 * @created 08/05/2024
 */
@Component
@EnableScheduling
@RequiredArgsConstructor
public class KafkaMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaMonitor.class);
    private final IKafkaService kafkaService;
    private final JdbcTemplate jdbcTemplate;

    private volatile boolean previousKafkaHealth = false;
    private static final long INITIAL_DELAY = 60000;
    private static final long HEARTBEAT_INTERVAL = 10000;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Scheduled(initialDelay = INITIAL_DELAY, fixedRate = HEARTBEAT_INTERVAL)
    public void sendHeartBeat() {
        boolean isKafkaHealthy = checkKafkaHealth();
        if (isKafkaHealthy != previousKafkaHealth) {
            LOG.debug("Change in Hazelcast cluster status...");

            if (!isKafkaHealthy) {
                handleUnhealthyKafka();
            } else {
                handleHealthyKafka();
            }

            previousKafkaHealth = isKafkaHealthy;
        }
    }

    private boolean checkKafkaHealth() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (Producer<String, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>("dummy-topic", "dummy-message");
            producer.send(record).get();

            return true;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            LOG.error("Error checking Kafka health: {}", e.getMessage());
            return false;
        }
    }

    private void handleHealthyKafka() {
        LOG.info("Healthy Kafka detected. Reattempting to send failed messages");
        HealthStatus.setKafkaHealthy(true);
        retryFailedMessages();
    }

    private void handleUnhealthyKafka() {
        LOG.info("Unhealthy Kafka detected. Defaulting to local cluster instance");
        HealthStatus.setKafkaHealthy(false);
    }

    private void retryFailedMessages() {
        try {
            String sql = "SELECT "
                    + KafkaDataAccess.COL_ID
                    + ", "
                    + KafkaDataAccess.COL_TOPIC
                    + ", "
                    + "CAST(" + KafkaDataAccess.COL_MESSAGE_DATA + " AS TEXT)"
                    + " FROM "
                    + KafkaDataAccess.TABLE_NAME;

            List<KafkaUnsentMessage> unsentMessageList = jdbcTemplate.query(sql, (rs, rowNum) -> {
                KafkaUnsentMessage unsentMessage = new KafkaUnsentMessage();
                unsentMessage.setId(rs.getInt(KafkaDataAccess.COL_ID));
                unsentMessage.setTopic(rs.getString(KafkaDataAccess.COL_TOPIC));
                unsentMessage.setMessageData(rs.getString(KafkaDataAccess.COL_MESSAGE_DATA));
                return unsentMessage;
            });

            for (KafkaUnsentMessage unsentMessage : unsentMessageList) {
                kafkaService.sendMessage(unsentMessage.getTopic(), unsentMessage.getMessageData());
                deleteMessageFromTable(unsentMessage.getId());
            }

        } catch (Exception e) {
            LOG.error("Failed to retry failed messages: {}", e.getMessage(), e);
        }
    }

    private void deleteMessageFromTable(int messageId) {
        try {
            String deleteQuery = "DELETE FROM "
                    + KafkaDataAccess.TABLE_NAME
                    + " WHERE "
                    + KafkaDataAccess.COL_ID + " = ?";

            jdbcTemplate.update(deleteQuery, messageId);
            LOG.debug("Successfully deleted message with ID {} from the table.", messageId);
        } catch (Exception e) {
            LOG.error("Failed to delete message with ID {} from the table: {}", messageId, e.getMessage(), e);
        }
    }

}
