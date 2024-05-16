package com.rahim.common.config.health;

/**
 * @author Rahim Ahmed
 * @created 15/05/2024
 */
public class HealthStatus {

    private HealthStatus() {}

    public static volatile boolean isHzHealthy = true;
    public static volatile boolean isKafkaHealthy = true;

    public static void setHzHealthy(boolean hzHealthy) {
        isHzHealthy = hzHealthy;
    }

    public static void setKafkaHealthy(boolean kafkaHealthy) {
        isKafkaHealthy = kafkaHealthy;
    }
}
