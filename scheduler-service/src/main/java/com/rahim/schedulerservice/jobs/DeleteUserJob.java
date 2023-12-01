package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.kafka.IKafkaService;
import com.rahim.schedulerservice.model.TimerInfo;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class DeleteUserJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteUserJob.class);
    private final IKafkaService kafkaService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(DeleteUserJob.class.getSimpleName());
        OffsetDateTime jobTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        LOG.info(timerInfo.getCallbackData());

        kafkaService.sendMessage("user-service-cleanup-accounts", "Start Delete Job: " + jobTime);
    }
}
