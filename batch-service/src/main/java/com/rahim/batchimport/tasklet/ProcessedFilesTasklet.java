package com.rahim.batchimport.tasklet;

import com.rahim.batchimport.constants.FileConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessedFilesTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessedFilesTasklet.class);

    @Value(FileConstants.GOLD_DATA_FILE_PATH)
    private Resource file;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            String sql = "INSERT INTO rgts.processed_files (file_name, processing_status) VALUES (?, ?)";
            String fileName = String.valueOf(file.getFilename());
            String status = "PROCESSED";

            jdbcTemplate.update(sql, fileName, status);

            LOG.info("Inserted data into processed_files table for file: {}", fileName);

        } catch (DataAccessException e) {
            LOG.error("Error processing file and inserting data into the database. File: {}", file.getFilename(), e);
            throw new RuntimeException("Error processing file and inserting data into the database", e);
        } catch (Exception e) {
            LOG.error("Unexpected error occurred", e);
            throw new RuntimeException("Unexpected error occurred", e);
        }
        return RepeatStatus.FINISHED;
    }
}
