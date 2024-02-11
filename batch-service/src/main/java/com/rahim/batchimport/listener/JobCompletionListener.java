package com.rahim.batchimport.listener;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("Batch Job Complete!");

            Long rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rgts.gold_price_history", Long.class);

            LOG.info("Total rows in the database: {}", rowCount);
        }
    }
}
