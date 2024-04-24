package com.rahim.schedulerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@Configuration
public class CronJobConfig {

    @Value("${cron.job.initial-delay}")
    private Long initialDelay;

    @Bean
    public Map<String, String> cronJobSchedules() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Long initialDelay() {
        return initialDelay;
    }

}
