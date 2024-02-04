package com.rahim.batchimport.processor;

import com.rahim.batchimport.model.GoldData;
import com.rahim.batchimport.model.GoldPriceHistory;
import com.rahim.batchimport.util.GoldPriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GoldDataProcessor implements ItemProcessor<GoldData, GoldPriceHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(GoldDataProcessor.class);

    @Override
    public GoldPriceHistory process(GoldData goldData) {
        try {
            BigDecimal priceOunce = new BigDecimal(goldData.getPrice());
            LocalDate effectiveDate = LocalDate.parse(goldData.getDate());

            BigDecimal pricePerGram = GoldPriceCalculator.getInstance().calculatePricePerGram(priceOunce);

            return new GoldPriceHistory(priceOunce, pricePerGram, effectiveDate);
        } catch (NumberFormatException | DateTimeParseException e) {
            LOG.error("Error processing GoldData: {}", goldData, e);
            return null;
        }
    }
}
