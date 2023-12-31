package com.rahim.schedulerservice.service.implementation;

import com.rahim.schedulerservice.model.TimerInfo;
import com.rahim.schedulerservice.service.ISchedulerService;

import com.rahim.schedulerservice.util.SchedulerFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SchedulerService implements ISchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler;

    public void schedule(final Class clazz, final TimerInfo info) {
        final JobDetail jobDetail = SchedulerFactory.buildJobDetails(clazz, info);
        final Trigger trigger = SchedulerFactory.buildTrigger(clazz, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("An error has occurred with the scheduler: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            LOG.error("An error has occurred with starting the scheduler: {}", e.getMessage());
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error("An error has occurred with shutting the scheduler: {}", e.getMessage());
        }
    }
}
