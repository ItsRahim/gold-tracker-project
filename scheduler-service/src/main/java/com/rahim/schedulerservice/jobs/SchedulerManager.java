package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.constant.CronJobName;
import com.rahim.schedulerservice.constant.KafkaTopic;
import com.rahim.schedulerservice.dao.CronJobDataAccess;
import com.rahim.schedulerservice.kafka.IKafkaService;
import com.rahim.schedulerservice.repository.CronJobRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rahim Ahmed
 * @created 26/04/2024
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerManager implements SchedulingConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerManager.class);
    private final CronJobRepository cronJobRepository;
    private final IKafkaService kafkaService;
    private final KafkaTopic kafkaTopic;
    private final Map<String, String> cronJobSchedules = new ConcurrentHashMap<>();
    private final Map<String, AtomicBoolean> jobExecutionStatus = new ConcurrentHashMap<>();
    private static final String CRON_JOB_MESSAGE = "Initiate Cron Job";

    @PostConstruct
    private void init() {
        initialiseCronJobSchedules();
    }

    /**
     * Retrieves a list of cron job names schedules from the database
     * Sets jobExecution status to 'false' indicating job is not running
     */
    private void initialiseCronJobSchedules() {
        List<Map<String, String>> schedulesFromDBList = cronJobRepository.getCronJobSchedule();
        schedulesFromDBList.forEach(scheduleMap -> {
            String cronJobName = scheduleMap.get(CronJobDataAccess.COL_CRON_JOB_NAME);
            String cronSchedule = scheduleMap.get(CronJobDataAccess.COL_CRON_JOB_SCHEDULE);
            cronJobSchedules.put(cronJobName, cronSchedule);
            jobExecutionStatus.put(cronJobName, new AtomicBoolean(false));
        });
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        cronJobSchedules.forEach((jobName, cronExpression) -> {
            if (!cronExpression.isEmpty()) {
                Runnable task = getTaskByJobName(jobName);
                if (task != null) {
                    taskRegistrar.addCronTask(task, cronExpression);
                    LOG.info("Scheduled task '{}' with cron expression '{}'", jobName, cronExpression);
                } else {
                    LOG.warn("No task found for job '{}'. Task not scheduled.", jobName);
                }
            } else {
                LOG.warn("Cron expression is empty for job '{}'. Task not scheduled.", jobName);
            }
        });
    }

    /**
     * Periodically, based off fixedDelayString, checks the cron_jobs table for any updates made to job configurations.
     * Updates the internal schedule of cron jobs accordingly.
     */
    @Scheduled(fixedDelayString = "#{@dbRefreshInterval}")
    private void updateCronJobSchedule() {
        LOG.debug("Checking database for cron job property updates...");
        initialiseCronJobSchedules();
        configureTasks(new ScheduledTaskRegistrar());
    }

    /**
     * Retrieves a Runnable task associated with the specified jobName.
     *
     * @param jobName the name of the job for which to retrieve the task
     * @return a Runnable task corresponding to the provided jobName, or null if no task is found
     */
    private Runnable getTaskByJobName(String jobName) {
        return switch (jobName) {
            case CronJobName.USER_CLEANUP_JOB -> this::accountCleanupJob;
            case CronJobName.UPDATE_GOLD_PRICE_JOB -> this::updateGoldPriceJob;
            case CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB -> this::updateGoldPriceHistoryJob;
            default -> null;
        };
    }

    /**
     * --------------------------------------------------------
     * JOB CONFIGURATIONS
     * --------------------------------------------------------
     */

    private void accountCleanupJob() {
        if (jobExecutionStatus.get(CronJobName.USER_CLEANUP_JOB).compareAndSet(false, true)) {
            LOG.info("Running " +  CronJobName.USER_CLEANUP_JOB);
            kafkaService.sendMessage(kafkaTopic.getCleanupTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.USER_CLEANUP_JOB).set(false);
        } else {
            LOG.warn(CronJobName.USER_CLEANUP_JOB + " is already running. Skipping job execution");
        }
    }

    private void updateGoldPriceJob() {
        if (jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_JOB).compareAndSet(false, true)) {
            LOG.info("Running " +  CronJobName.UPDATE_GOLD_PRICE_JOB);
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).set(false);
        } else {
            LOG.warn(CronJobName.UPDATE_GOLD_PRICE_JOB + " is already running. Skipping job execution");
        }
    }

    private void updateGoldPriceHistoryJob() {
        if (jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).compareAndSet(false, true)) {
            LOG.info("Running " +  CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB);
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceHistoryTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).set(false);
        } else {
            LOG.warn(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB + " is already running. Skipping job execution");
        }
    }

}
