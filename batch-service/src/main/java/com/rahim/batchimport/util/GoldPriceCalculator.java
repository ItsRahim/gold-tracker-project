package com.rahim.batchimport.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GoldPriceCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCalculator.class);
    private static final double GRAMS_PER_TROY_OUNCE = 31.1035;
    private static final int SCALE = 2;

    public static GoldPriceCalculator getInstance() {
        return new GoldPriceCalculator();
    }

    public BigDecimal calculatePricePerGram(BigDecimal pricePerOunce) {
        try {
            BigDecimal ouncesBigDecimal = pricePerOunce.setScale(SCALE, RoundingMode.HALF_UP);
            BigDecimal gramsPerOunce = new BigDecimal(GRAMS_PER_TROY_OUNCE);

            return ouncesBigDecimal.divide(gramsPerOunce, SCALE, RoundingMode.HALF_UP);

        } catch (ArithmeticException ex) {
            LOG.error("Error converting price per ounce to per gram: {}", ex.getMessage());
            throw new RuntimeException("Error converting price per ounce to per gram", ex);
        }
    }
}