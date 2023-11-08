package com.rahim.schedulerservice.util;

import com.rahim.schedulerservice.model.TimerInfo;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SchedulerFactory {
    private SchedulerFactory() {}

    public static JobDetail buildJobDetails(final Class clazz, final TimerInfo info) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(clazz.getSimpleName(), info);

        return JobBuilder
                .newJob(clazz)
                .withIdentity(clazz.getSimpleName())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final Class clazz, final TimerInfo info) {
        SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(info.getRepeatIntervalMs());

        if (info.isRunForever()) {
            builder = builder.repeatForever();
        } else {
            builder = builder.withRepeatCount(info.getTotalFireCount() - 1);
        }

        return TriggerBuilder
                .newTrigger()
                .withIdentity(clazz.getSimpleName())
                .withSchedule(builder)
                .startAt(new Date(System.currentTimeMillis() + info.getInitialOffsetMs()))
                .build();
    }
}
