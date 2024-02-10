package com.rahim.batchimport.writer;

import com.rahim.batchimport.config.DatasourceConfig;
import com.rahim.batchimport.model.GoldPriceHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoldPriceHistoryWriter {

    private final DatasourceConfig datasourceConfig;

    public static final String PRICE_HISTORY_QUERY = "INSERT INTO rgts.gold_price_history (price_ounce, price_gram, effective_date) VALUES (:priceOunce, :priceGram, :effectiveDate)";

    @Bean
    public JdbcBatchItemWriter<GoldPriceHistory> goldPriceWriter() {
        return new JdbcBatchItemWriterBuilder<GoldPriceHistory>()
                .sql(PRICE_HISTORY_QUERY)
                .dataSource(datasourceConfig.dataSource())
                .beanMapped()
                .build();
    }
}
