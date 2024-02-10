package com.rahim.batchimport.config;

import com.rahim.batchimport.listener.CustomItemReaderListener;
import com.rahim.batchimport.listener.CustomItemWriterListener;
import com.rahim.batchimport.listener.JobCompletionNotificationListener;
import com.rahim.batchimport.listener.StepSkipListener;
import com.rahim.batchimport.model.GoldData;
import com.rahim.batchimport.model.GoldPriceHistory;
import com.rahim.batchimport.policies.PriceSkipPolicy;
import com.rahim.batchimport.processor.GoldDataProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends AbstractBatchConfig {

    @Value("classpath:xaugbp-history.csv")
    private Resource goldDataFileResource;

    @Autowired
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        super(jobRepository, transactionManager);
    }

    @Bean
    public GoldDataProcessor goldDataProcessor() {
        return new GoldDataProcessor();
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
    public FlatFileItemReader<GoldData> goldDataReader() {
        return new FlatFileItemReaderBuilder<GoldData>()
                .name("goldDataReader")
                .resource(goldDataFileResource)
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutorBuilder()
                .concurrencyLimit(chunkSize)
                .build();
    }

    @Bean
    public Step importStep(@Qualifier("goldDataReader") FlatFileItemReader<GoldData> goldDataReader,
                           GoldDataProcessor goldDataProcessor,
                           @Qualifier("goldPriceWriter") JdbcBatchItemWriter<GoldPriceHistory> goldPriceWriter,
                           @Qualifier("taskExecutor") TaskExecutor taskExecutor,
                           CustomItemReaderListener customItemReaderListener,
                           CustomItemWriterListener customItemWriterListener,
                           StepSkipListener stepSkipListener) {
        return new StepBuilder("csvImport", jobRepository)
                .<GoldData, GoldPriceHistory>chunk(10, transactionManager)
                .reader(goldDataReader)
                .processor(goldDataProcessor)
                .writer(goldPriceWriter)
                .taskExecutor(taskExecutor)
                .faultTolerant()
                .skipPolicy(priceSkipPolicy())
                .listener(stepSkipListener)
                .listener(customItemReaderListener)
                .listener(customItemWriterListener)
                .build();
    }

    @Bean
    public Job impportPriceJob(@Qualifier("importStep") Step importStep, JobCompletionNotificationListener listener) {
        return new JobBuilder("importPrice", jobRepository)
                .listener(listener)
                .start(importStep)
                .build();
    }

    private LineMapper<GoldData> lineMapper() {
        DefaultLineMapper<GoldData> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("date", "price");

        BeanWrapperFieldSetMapper<GoldData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(GoldData.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

}
