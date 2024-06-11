package com.rahim.common.config.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 15/11/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig implements KafkaConstants {

    private final KafkaConsumerProperties kafkaConsumerProperties;
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getKeyDeserializer());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getValueDeserializer());

        if (SSL.equalsIgnoreCase(kafkaProperties.getSecurityProtocol())) {
            configureSSL(consumerProps);
        } else {
            consumerProps.put(SECURITY_PROTOCOL, PLAINTEXT);
        }

        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }

    private void configureSSL(Map<String, Object> consumerProps) {
        consumerProps.put(SECURITY_PROTOCOL, SSL);
        consumerProps.put(SSL_TRUSTSTORE_LOCATION, kafkaProperties.getDecodedTruststore());
        consumerProps.put(SSL_TRUSTSTORE_PASSWORD, kafkaProperties.getKeystorePassword());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }
}