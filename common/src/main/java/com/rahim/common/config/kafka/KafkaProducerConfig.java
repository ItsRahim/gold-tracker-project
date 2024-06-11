package com.rahim.common.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 18/11/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig implements KafkaConstants {

    private final KafkaProducerProperties kafkaProducerProperties;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getKeySerializer());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getValueSerializer());

        if (SSL.equalsIgnoreCase(kafkaProperties.getSecurityProtocol())) {
            configureSSL(producerProps);
        } else {
            producerProps.put(SECURITY_PROTOCOL, PLAINTEXT);
        }

        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    private void configureSSL(Map<String, Object> producerProps) {
        producerProps.put(SECURITY_PROTOCOL, SSL);
        producerProps.put(SSL_KEYSTORE_LOCATION, kafkaProperties.getDecodedKeystore());
        producerProps.put(SSL_KEYSTORE_PASSWORD, kafkaProperties.getKeystorePassword());
        producerProps.put(SSL_TRUSTSTORE_LOCATION, kafkaProperties.getDecodedTruststore());
        producerProps.put(SSL_TRUSTSTORE_PASSWORD, kafkaProperties.getKeystorePassword());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}