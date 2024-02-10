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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GoldDataProcessor implements ItemProcessor<GoldData, GoldPriceHistory> {

    private static final Logger LOG = LoggerFactory.getLogger(GoldDataProcessor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final Set<LocalDate> existingDates = ConcurrentHashMap.newKeySet();

    private boolean isInitialised = false;

    @Override
    public GoldPriceHistory process(GoldData goldData) {
        initializeExistingDates();

        try {
            BigDecimal priceOunce = new BigDecimal(goldData.getPrice());
            LocalDate effectiveDate = LocalDate.parse(goldData.getDate());

            BigDecimal pricePerGram = GoldPriceCalculator.getInstance().calculatePricePerGram(priceOunce);

            if (existingDates.contains(effectiveDate)) {
                LOG.info("Skipping item with effective date {} as it already exists in the database", effectiveDate);
                throw new DuplicateEffectiveDateException("Duplicate effective date found: " + effectiveDate);
            }

            return new GoldPriceHistory(priceOunce, pricePerGram, effectiveDate);
        } catch (NumberFormatException | DateTimeParseException e) {
            LOG.error("Error processing GoldData: {}", goldData, e);
            return null;
        }
    }

    private void initializeExistingDates() {
        if (!isInitialised) {
            List<LocalDate> datesFromDatabase = jdbcTemplate.queryForList("SELECT DISTINCT effective_date FROM rgts.gold_price_history", LocalDate.class);

            if (!datesFromDatabase.isEmpty()) {
                existingDates.addAll(datesFromDatabase);
                isInitialised = true;
            }

        }
    }
}
