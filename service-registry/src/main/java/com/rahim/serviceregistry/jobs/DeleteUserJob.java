package com.rahim.serviceregistry.jobs;

import com.rahim.userservice.scheduler.model.TimerInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DeleteUserJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(DeleteUserJob.class.getSimpleName());
        log.info(timerInfo.getCallbackData());
    }
}
