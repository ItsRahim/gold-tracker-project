package com.rahim.schedulerservice.config;

import com.rahim.schedulerservice.model.CronJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
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

    @Value("${cron.job.db-refresh}")
    private Long dbRefresh;

    @Bean
    public Map<String, List<CronJob>> cronJobSchedules() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Long initialDelay() {
        return initialDelay;
    }

    @Bean
    public Long dbRefresh() {
        return dbRefresh;
    }

}
