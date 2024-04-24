package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.kafka.IKafkaService;
import com.rahim.schedulerservice.constant.KafkaTopic;
import com.rahim.schedulerservice.repository.CronJobRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
@Configuration
public class ScheduledJobs {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    private final CronJobRepository cronJobRepository;
    private final IKafkaService kafkaService;
    private final KafkaTopic kafkaTopic;

    private static final String CRON_JOB_MESSAGE = "Initiate Cron Job";
    private static final String TIME_ZONE = "UTC";

    private final Map<String, String> cronJobSchedules = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        initialiseCronJobSchedules();
    }

    private void initialiseCronJobSchedules() {
        Map<String, String> schedulesFromDB = cronJobRepository.getCronJobSchedule();
        cronJobSchedules.putAll(schedulesFromDB);
    }

    @Async
    @Scheduled(fixedRateString = "#{@cronJobSchedules['User Cleanup Job']}", zone = TIME_ZONE)
    public void accountCleanupJob() {
        try {
            kafkaService.sendMessage(kafkaTopic.getCleanupTopic(), CRON_JOB_MESSAGE);
            LOG.debug("Account cleanup job initiated successfully");
        } catch (Exception e) {
            LOG.error("Error occurred during account cleanup job: {}", e.getMessage());
        }
    }

    @Async
    @Scheduled(fixedRate = 1000, zone = TIME_ZONE)
    public void updateGoldPriceJob() {
        try {
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceTopic(), CRON_JOB_MESSAGE);
            LOG.debug("Gold price update job initiated successfully");
        } catch (Exception e) {
            LOG.error("Error occurred during gold price update job: {}", e.getMessage());
        }
    }

    @Async
    @Scheduled(fixedRate = 1000, zone = TIME_ZONE)
    public void updateGoldPriceHistoryJob() {
        try {
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceHistoryTopic(), CRON_JOB_MESSAGE);
            LOG.debug("Gold price history update job initiated successfully");
        } catch (Exception e) {
            LOG.error("Error occurred during gold price history update cron job: {}", e.getMessage());
        }
    }
}
