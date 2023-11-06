package com.rahim.userservice.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DeleteUserJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("HELLO WORLD!");
    }
}
