package com.rahim.schedulerservice.jobs;

import com.rahim.schedulerservice.constant.TopicConstants;
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
public class UpdateGoldPriceJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(UpdateGoldPriceJob.class);
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TimerInfo timerInfo = (TimerInfo) jobDataMap.get(UpdateGoldPriceJob.class.getSimpleName());
        OffsetDateTime jobTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        LOG.info(timerInfo.getCallbackData());

        kafkaService.sendMessage(topicConstants.getUpdatePriceTopic(), "Start Update Price Job: " + jobTime);
    }
}
