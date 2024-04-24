package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.constant.CronJobName;
import com.rahim.schedulerservice.dao.CronJobDataAccess;
import com.rahim.schedulerservice.kafka.IKafkaService;
import com.rahim.schedulerservice.constant.KafkaTopic;
import com.rahim.schedulerservice.repository.CronJobRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@Component
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledJobs {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledJobs.class);
    private final CronJobRepository cronJobRepository;
    private final IKafkaService kafkaService;
    private final KafkaTopic kafkaTopic;

    Map<String, String> cronJobSchedules = new ConcurrentHashMap<>();
    private static final String CRON_JOB_MESSAGE = "Initiate Cron Job";
    private static final String TIME_ZONE = "UTC";

    @PostConstruct
    private void init() {
        //initialiseCronJobSchedules();
    }

    /**
     * Method to set a map containing all active cron jobs from DB e.g. <CronJobName, CronJobInterval>
     */
    private void initialiseCronJobSchedules() {
        List<Map<String, String>> schedulesFromDBList = cronJobRepository.getCronJobSchedule();
        for (Map<String, String> scheduleMap : schedulesFromDBList) {
            String cronJobName = scheduleMap.get(CronJobDataAccess.COL_CRON_JOB_NAME);
            String cronSchedule = scheduleMap.get(CronJobDataAccess.COL_CRON_JOB_SCHEDULE);
            cronJobSchedules.put(cronJobName, cronSchedule);
        }
    }

    //@Scheduled(initialDelayString = "#{@dbRefreshInterval}")
    @Scheduled(cron = "0 * * * * *")
    private void updateCronJobSchedules() {
        LOG.debug("Checking database for cron job property updates...");
        initialiseCronJobSchedules();
    }

    //@Scheduled(cron = "#{@cronJobSchedules.get('User Cleanup Job')}", zone = TIME_ZONE, initialDelayString = "#{@initialDelay}")
    @Scheduled(cron = "0 */720 * * * *", zone = TIME_ZONE)
    public void accountCleanupJob() {
        if (isJobActive(CronJobName.USER_CLEANUP_JOB)) {
            try {
                kafkaService.sendMessage(kafkaTopic.getCleanupTopic(), CRON_JOB_MESSAGE);
                LOG.debug("Account cleanup job initiated successfully");
            } catch (Exception e) {
                LOG.error("Error occurred during account cleanup job: {}", e.getMessage());
            }
        }
    }

    //@Scheduled(cron = "#{@cronJobSchedules.get('Update Gold Price Job')}", zone = TIME_ZONE, initialDelayString = "#{@initialDelay}")
    @Scheduled(cron = "0 * * * * *", zone = TIME_ZONE)
    public void updateGoldPriceJob() {
        if (isJobActive(CronJobName.UPDATE_GOLD_PRICE_JOB)) {
            try {
                kafkaService.sendMessage(kafkaTopic.getUpdatePriceTopic(), CRON_JOB_MESSAGE);
                LOG.debug("Gold price update job initiated successfully");
            } catch (Exception e) {
                LOG.error("Error occurred during gold price update job: {}", e.getMessage());
            }
        }
    }

    //@Scheduled(cron = "#{@cronJobSchedules.get('Update Gold Price History Job')}", zone = TIME_ZONE, initialDelayString = "#{@initialDelay}")
    @Scheduled(cron = "0 59 23 * * *", zone = TIME_ZONE)
    public void updateGoldPriceHistoryJob() {
        if (isJobActive(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB)) {
            try {
                kafkaService.sendMessage(kafkaTopic.getUpdatePriceHistoryTopic(), CRON_JOB_MESSAGE);
                LOG.debug("Gold price history update job initiated successfully");
            } catch (Exception e) {
                LOG.error("Error occurred during gold price history update cron job: {}", e.getMessage());
            }
        }
    }

    /**
     * Method to check if cron job is active. Done by checking if given cron job name exists in map
     * @param jobName - name of cron job to be looked up
     * @return boolean indicating if job is active
     */
    private boolean isJobActive(String jobName) {
        return cronJobSchedules.containsKey(jobName);
    }
}
