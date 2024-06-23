package com.rahim.common.config.kafka;

import com.rahim.common.config.kafka.properties.KafkaProperties;
import com.rahim.common.config.kafka.properties.KafkaSSLProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rahim Ahmed
 * @created 23/06/2024
 */
@RequiredArgsConstructor
public class KafkaBaseConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaBaseConfig.class);
    protected final KafkaSSLProperties kafkaSSLProperties;
    protected final KafkaProperties kafkaProperties;

    public boolean isSSLEnabled() {
        String securityProtocol = kafkaProperties.getSecurityProtocol();
        return securityProtocol != null && securityProtocol.equalsIgnoreCase("SSL");
    }

    public void validateSSLProps(Map<String, Object> kafkaProperties) {
        boolean anyNull = kafkaProperties
                .values()
                .stream()
                .anyMatch(Objects::isNull);

        if (anyNull) {
            List<String> nullFields = kafkaProperties.entrySet().stream()
                    .filter(entry -> entry.getValue() == null)
                    .map(Map.Entry::getKey)
                    .toList();
            log.error("Null SSL properties detected: {}. Verify property values in appConfig.yml", nullFields);
            throw new IllegalArgumentException("Incomplete Kafka SSL configuration. Properties must not be null");
        }
    }
}
