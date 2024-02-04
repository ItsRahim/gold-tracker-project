package com.rahim.batchimport.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
public abstract class AbstractBatchConfig {
    protected final JobRepository jobRepository;
    protected  PlatformTransactionManager transactionManager;
}
