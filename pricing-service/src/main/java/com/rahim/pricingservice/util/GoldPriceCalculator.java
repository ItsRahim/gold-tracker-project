package com.rahim.pricingservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoldPriceCalculator {
    private static final Logger log = LoggerFactory.getLogger(GoldPriceCalculator.class);

    public double calculatePricePerGram(double pricePerOunce) {
        try {
            double GRAMS_PER_TROY_OUNCE = 31.1035;
            double pricePerGram = pricePerOunce / GRAMS_PER_TROY_OUNCE;

            log.info("Conversion successful: {} per ounce is {} per gram", pricePerOunce, pricePerGram);

            return pricePerGram;
        } catch (ArithmeticException ex) {
            log.error("Error converting price per ounce to per gram: {}", ex.getMessage());
            throw new RuntimeException("Error converting price per ounce to per gram", ex);
        }
    }

}
