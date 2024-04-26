package com.rahim.schedulerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@Configuration
public class CronJobConfig {

    @Value("${cron.job.db-refresh-interval}")
    private String dbRefreshInterval;

    @Bean
    public String dbRefreshInterval() {
        return dbRefreshInterval;
    }

}
