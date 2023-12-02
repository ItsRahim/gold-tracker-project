package com.rahim.schedulerservice.service;

import com.rahim.schedulerservice.jobs.DeleteUserJob;
import com.rahim.schedulerservice.jobs.UpdateGoldPriceJob;
import com.rahim.schedulerservice.model.TimerInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private static final Logger LOG = LoggerFactory.getLogger(JobService.class);
    private final ISchedulerService schedulerService;

    @PostConstruct
    public void scheduleJobsAtStartup() {
        runUserCleanupJob();
        runUpdateGoldPriceJob();
    }

    public void runUserCleanupJob() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(-1)
                .runForever(true)
                .repeatIntervalMs(12 * 60 * 60 * 1000)
                .initialOffsetMs(1000)
                .callbackData("User Microservice Job")
                .build();
        schedulerService.schedule(DeleteUserJob.class, info);
    }

    public void runUpdateGoldPriceJob() {
        final TimerInfo info = TimerInfo.builder()
                .totalFireCount(-1)
                .runForever(true)
                .repeatIntervalMs(60000)
                .initialOffsetMs(1000)
                .callbackData("User Microservice Job")
                .build();
        schedulerService.schedule(UpdateGoldPriceJob.class, info);
    }
}
