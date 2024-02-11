package com.rahim.batchimport.processor;

import com.rahim.batchimport.exception.DuplicateEffectiveDateException;
import com.rahim.batchimport.model.GoldData;
import com.rahim.batchimport.model.GoldPriceHistory;
import com.rahim.batchimport.util.GoldPriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GoldDataProcessor implements ItemProcessor<GoldData, GoldPriceHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(GoldDataProcessor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public GoldPriceHistory process(GoldData goldData) {
        try {
            BigDecimal priceOunce = new BigDecimal(goldData.getPrice());
            LocalDate effectiveDate = LocalDate.parse(goldData.getDate());
            BigDecimal pricePerGram = GoldPriceCalculator.getInstance().calculatePricePerGram(priceOunce);

            if (isEffectiveDateAlreadyExists(effectiveDate) || LocalDate.now().isEqual(effectiveDate)) {
                LOG.debug("Skipping item with effective date {} as it already exists in the database", effectiveDate);
                throw new DuplicateEffectiveDateException("Duplicate effective date found: " + effectiveDate);
            }

            return new GoldPriceHistory(priceOunce, pricePerGram, effectiveDate);
        } catch (NumberFormatException | DateTimeParseException e) {
            LOG.error("Error processing GoldData: {}", goldData, e);
            return null;
        }
    }

    private boolean isEffectiveDateAlreadyExists(LocalDate effectiveDate) {
        String sql = "SELECT COUNT(*) FROM rgts.gold_price_history WHERE effective_date = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, effectiveDate);

        return count != null && count > 0;
    }

}
