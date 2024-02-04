package com.rahim.batchimport.processor;

import com.rahim.batchimport.model.GoldData;
import com.rahim.batchimport.model.GoldPriceHistory;
import com.rahim.batchimport.util.GoldPriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.math.BigDecimal;

public class GoldDataProcessor implements ItemProcessor<GoldData, GoldPriceHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(GoldDataProcessor.class);

    @Override
    public GoldPriceHistory process(GoldData goldData) {
        BigDecimal pricePerGram = GoldPriceCalculator.getInstance().calculatePricePerGram(goldData.getPriceOunce());
        GoldPriceHistory goldPriceHistory = new GoldPriceHistory(goldData.getPriceOunce(), pricePerGram, goldData.getEffectiveDate());

        LOG.info("Converting (" + goldData + ") into (" + goldPriceHistory + ")");

        return goldPriceHistory;
    }
}
