package com.rahim.common.config.kafka;

import com.rahim.common.config.health.HealthCheckAspect;
import com.rahim.common.service.kafka.KafkaFailover;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    private final HealthCheckAspect healthCheckAspect;

    private volatile boolean previousKafkaHealth = false;
    private static final long INITIAL_DELAY = 60000;
    private static final long HEARTBEAT_INTERVAL = 10000;
    private static final String DUMMY_TOPIC = "dummy-topic";
    private static final String DUMMY_MESSAGE = "dummy-message";

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
            ProducerRecord<String, String> record = new ProducerRecord<>(DUMMY_TOPIC, DUMMY_MESSAGE);
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
        healthCheckAspect.setKafkaHealthy(true);
    }

    private void handleUnhealthyKafka() {
        LOG.info("Unhealthy Kafka detected. Defaulting to local cluster instance");
        healthCheckAspect.setKafkaHealthy(false);
    }

}
