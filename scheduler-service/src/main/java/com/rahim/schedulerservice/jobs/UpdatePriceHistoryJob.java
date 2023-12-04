package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.kafka.IKafkaService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UpdatePriceHistoryJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(UpdatePriceHistoryJob.class);
    private final IKafkaService kafkaService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String callbackData = (String) jobDataMap.get(UpdatePriceHistoryJob.class.getSimpleName());

        LOG.info(callbackData);

        kafkaService.sendMessage("pricing-service-history-table", "Update Gold Price History Table: ");
    }
}
