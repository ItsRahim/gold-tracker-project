package com.rahim.schedulerservice.service.implementation;

import com.rahim.schedulerservice.model.TimerInfo;
import com.rahim.schedulerservice.service.ISchedulerService;

import com.rahim.schedulerservice.util.SchedulerFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulerService implements ISchedulerService {
    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final Scheduler scheduler;

    public void schedule(final Class clazz, final TimerInfo info) {
        final JobDetail jobDetail = SchedulerFactory.buildJobDetails(clazz, info);
        final Trigger trigger = SchedulerFactory.buildTrigger(clazz, info);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("An error has occurred with the scheduler: {}", e.getMessage());
        }
    }

    @Override
    public List<TimerInfo> getAllRunningTimers() {
        try {
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            return (TimerInfo) jobDetail.getJobDataMap().get(jobKey.getName());
                        } catch (SchedulerException e) {
                            log.error("Some error has occurred: {}", e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch(SchedulerException e) {
            log.error("An error has occurred fetching jobs: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public TimerInfo getRunningTimer(String timerId) {
        return null;
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
