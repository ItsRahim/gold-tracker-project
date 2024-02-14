package com.rahim.batchimport.config;

import com.rahim.batchimport.constants.FileConstants;
import com.rahim.batchimport.listener.CustomItemReaderListener;
import com.rahim.batchimport.listener.CustomItemWriterListener;
import com.rahim.batchimport.listener.JobCompletionListener;
import com.rahim.batchimport.listener.StepSkipListener;
import com.rahim.batchimport.model.GoldData;
import com.rahim.batchimport.model.GoldPriceHistory;
import com.rahim.batchimport.policies.SkipPolicy;
import com.rahim.batchimport.processor.GoldDataProcessor;
import com.rahim.batchimport.tasklet.ProcessedFilesTasklet;
import lombok.Getter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Getter
@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class JobConfig extends BaseBatchConfig {

    @Value(FileConstants.GOLD_DATA_FILE_PATH)
    private Resource goldDataFileResource;

    private final ProcessedFilesTasklet processedFilesTasklet;

    @Autowired
    public JobConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, ProcessedFilesTasklet processedFilesTasklet) {
        super(jobRepository, transactionManager);
        this.processedFilesTasklet = processedFilesTasklet;
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
    public Step importStep(@Qualifier("goldDataReader") ItemReader<GoldData> goldDataReader,
                           GoldDataProcessor goldDataProcessor,
                           @Qualifier("goldPriceWriter") JdbcBatchItemWriter<GoldPriceHistory> goldPriceWriter,
                           @Qualifier("taskExecutor") TaskExecutor taskExecutor,
                           CustomItemReaderListener customItemReaderListener,
                           SkipPolicy skipPolicy,
                           CustomItemWriterListener customItemWriterListener,
                           StepSkipListener stepSkipListener) {
        return new StepBuilder("csvImport", jobRepository)
                .<GoldData, GoldPriceHistory>chunk(chunkSize, transactionManager)
                .reader(goldDataReader)
                .processor(goldDataProcessor)
                .writer(goldPriceWriter)
                .taskExecutor(taskExecutor)
                .faultTolerant()
                .skipPolicy(skipPolicy)
                .listener(stepSkipListener)
                .listener(customItemReaderListener)
                .listener(customItemWriterListener)
                .build();
    }

    @Bean
    public Step processedFilesStep() {
        return new StepBuilder("processedFileStep", jobRepository)
                .tasklet(processedFilesTasklet, transactionManager)
                .build();
    }

    @Bean
    public Job importPriceJob(@Qualifier("importStep") Step importStep,
                              @Qualifier("processedFilesStep") Step processedFilesStep,
                              JobCompletionListener listener) {
        return new JobBuilder("importPrice", jobRepository)
                .listener(listener)
                .start(importStep)
                .next(processedFilesStep)
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
