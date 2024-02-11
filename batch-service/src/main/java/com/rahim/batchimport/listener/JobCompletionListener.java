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
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionListener.class);
    private final JdbcTemplate jdbcTemplate;
    Long dataBefore;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.STARTING) {
            LOG.info("Batch Job Starting!");

            dataBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rgts.gold_price_history", Long.class);

            LOG.info("Total rows in the database before job: {}", dataBefore);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("Batch Job Complete!");

            Long dataAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM rgts.gold_price_history", Long.class);
            dataAfter = (dataAfter != null) ? dataAfter : 0L;

            Long addedRows = dataAfter - dataBefore;

            LOG.info("Added rows during job execution: {}", addedRows);
        }
    }

}
