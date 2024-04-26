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
        LOG.debug("Updating cron jobs...");
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

    @Scheduled(fixedDelayString = "#{@dbRefreshInterval}")
    private void updateCronJobSchedule() {
        LOG.debug("Checking database for cron job property updates...");
        initialiseCronJobSchedules();
        configureTasks(new ScheduledTaskRegistrar());
    }

    private Runnable getTaskByJobName(String jobName) {
        return switch (jobName) {
            case CronJobName.USER_CLEANUP_JOB -> this::accountCleanupJob;
            case CronJobName.UPDATE_GOLD_PRICE_JOB -> this::updateGoldPriceJob;
            case CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB -> this::updateGoldPriceHistoryJob;
            default -> null;
        };
    }

    private void accountCleanupJob() {
        if (jobExecutionStatus.get(CronJobName.USER_CLEANUP_JOB).compareAndSet(false, true)) {
            LOG.info("Running account cleanup job");
            kafkaService.sendMessage(kafkaTopic.getCleanupTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.USER_CLEANUP_JOB).set(false);
        }
    }

    private void updateGoldPriceJob() {
        if (jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_JOB).compareAndSet(false, true)) {
            LOG.info("Running update gold price job");
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).set(false);
        }
    }

    private void updateGoldPriceHistoryJob() {
        if (jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).compareAndSet(false, true)) {
            LOG.info("Running update gold price history job");
            kafkaService.sendMessage(kafkaTopic.getUpdatePriceHistoryTopic(), CRON_JOB_MESSAGE);
            jobExecutionStatus.get(CronJobName.UPDATE_GOLD_PRICE_HISTORY_JOB).set(false);
        }
    }

}
