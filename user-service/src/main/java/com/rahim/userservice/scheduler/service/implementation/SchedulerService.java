package com.rahim.userservice.scheduler.service.implementation;

import com.rahim.userservice.scheduler.model.TimerInfo;
import com.rahim.userservice.scheduler.service.ISchedulerService;
import com.rahim.userservice.scheduler.config.SchedulerConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService implements ISchedulerService {
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler;

    public void schedule(final Class clazz, final TimerInfo info) {
        final JobDetail jobDetail = SchedulerConfiguration.buildJobDetails(clazz, info);
        final Trigger trigger = SchedulerConfiguration.buildTrigger(clazz, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("An error has occurred with the scheduler: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("An error has occurred with starting the scheduler: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            log.error("An error has occurred with shutting the scheduler: {}", e.getMessage());
        }
    }
}
