package com.rahim.schedulerservice.service;

import com.rahim.schedulerservice.jobs.DeleteUserJob;
import com.rahim.schedulerservice.jobs.UpdateGoldPriceJob;
import com.rahim.schedulerservice.model.TimerInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);
    private final ISchedulerService schedulerService;

    @PostConstruct
    public void scheduleJobsAtStartup() {
        runUserCleanupJob();
        runUpdateGoldPriceJob();
        runUpdatePriceHistoryJob();
    }

    public void runUserCleanupJob() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(-1)
                .runForever(true)
                .repeatIntervalMs(12 * 60 * 60 * 1000)
                .initialOffsetMs(1000)
                .callbackData("User Microservice Job - Cleanup Accounts")
                .build();
        schedulerService.schedule(DeleteUserJob.class, info);
    }

    public void runUpdateGoldPriceJob() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(-1)
                .runForever(true)
                .repeatIntervalMs(60000)
                .initialOffsetMs(1000)
                .callbackData("Pricing Microservice Job - Update Gold Prices")
                .build();
        schedulerService.schedule(UpdateGoldPriceJob.class, info);
    }

    public void runUpdatePriceHistoryJob() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(-1)
                .runForever(true)
                .repeatIntervalMs(24 * 60 * 60 * 1000)
                .initialOffsetMs(calculateInitialOffsetForNightlyRun())
                .callbackData("Pricing Microservice Job - Update Gold Prices")
                .build();
        schedulerService.schedule(UpdateGoldPriceJob.class, info);
    }

    private long calculateInitialOffsetForNightlyRun() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = LocalDateTime.of(now.toLocalDate(), LocalTime.of(23, 59));

        if (nextRun.isBefore(now)) {
            nextRun = nextRun.plusDays(1);
        }

        Duration duration = Duration.between(now, nextRun);
        return duration.toMillis();
    }
}
