package com.rahim.pricingservice.util;

import com.rahim.pricingservice.enums.GoldPurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoldPriceCalculator {
    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCalculator.class);
    private double pricePerGram;

    public double calculatePricePerGram(double pricePerOunce) {
        try {
            double GRAMS_PER_TROY_OUNCE = 31.1035;
            pricePerGram = pricePerOunce / GRAMS_PER_TROY_OUNCE;

            LOG.info("Conversion successful: {} per ounce is {} per gram", pricePerOunce, pricePerGram);

            return pricePerGram;
        } catch (ArithmeticException ex) {
            LOG.error("Error converting price per ounce to per gram: {}", ex.getMessage());
            throw new RuntimeException("Error converting price per ounce to per gram", ex);
        }
    }

    public double calculateGoldPriceByCarat(String carat) {
        try {
            double purity = GoldPurity.getPurityByCarat(carat);
            return purity * pricePerGram;
        } catch (IllegalArgumentException e) {
            LOG.error("Error calculating gold price for carat {}: {}", carat, e.getMessage());
            return 0;
        }
    }

    public double calculateGoldPrice(double caratPurity, double netWeight, double pricePerGram) {
        if (caratPurity < 0 || caratPurity > 1 || netWeight < 0 || pricePerGram < 0) {
            LOG.error("Invalid input values. Carat purity: {}, Net weight: {}, Price per gram: {}", caratPurity, netWeight, pricePerGram);
            throw new IllegalArgumentException("Invalid input values. Carat purity, net weight, and price per gram must be non-negative.");
        }
    
        double goldPrice = caratPurity * netWeight * pricePerGram;
        LOG.info("Calculated Gold Price: Carat Purity: {}, Net Weight: {} grams, Price per gram: ${}, Total Price: ${}", caratPurity, netWeight, pricePerGram, goldPrice);

        return goldPrice;
    }
}
