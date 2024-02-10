package com.rahim.batchimport.config;

import com.rahim.batchimport.listener.CustomItemReaderListener;
import com.rahim.batchimport.listener.CustomItemWriterListener;
import com.rahim.batchimport.listener.StepSkipListener;
import com.rahim.batchimport.policies.PriceSkipPolicy;
import com.rahim.batchimport.processor.DataCleanerProcessor;
import com.rahim.batchimport.processor.GoldDataProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
public abstract class BaseBatchConfig {

    protected final JobRepository jobRepository;
    protected final PlatformTransactionManager transactionManager;

    @Value("${processing.chunk-size}")
    protected int chunkSize;

    @Bean
    public GoldDataProcessor goldDataProcessor() {
        return new GoldDataProcessor();
    }

    @Bean
    public DataCleanerProcessor dataCleanerProcessor() {
        return new DataCleanerProcessor();
    }

    @Bean
    public PriceSkipPolicy priceSkipPolicy() {
        return new PriceSkipPolicy();
    }

    @Bean
    public StepSkipListener stepSkipListener() {
        return new StepSkipListener();
    }

    @Bean
    public CustomItemWriterListener itemWriterListener() {
        return new CustomItemWriterListener();
    }

    @Bean
    public CustomItemReaderListener itemReaderListener() {
        return new CustomItemReaderListener();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutorBuilder()
                .concurrencyLimit(chunkSize)
                .build();
    }

}
