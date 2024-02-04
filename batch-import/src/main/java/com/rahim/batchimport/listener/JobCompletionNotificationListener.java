package com.rahim.batchimport.listener;

import com.rahim.batchimport.model.GoldPriceHistory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOG = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOG.info("Batch Job Complete!");

            jdbcTemplate
                    .query("SELECT price_ounce, price_gram, effective_date FROM rgts.gold_price_history", new DataClassRowMapper<>(GoldPriceHistory.class))
                    .forEach(goldPriceHistory -> LOG.info("Found <{{}}> in the database.", goldPriceHistory));
        }
    }
}
