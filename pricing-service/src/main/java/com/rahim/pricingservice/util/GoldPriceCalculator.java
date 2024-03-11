package com.rahim.pricingservice.util;

import com.rahim.pricingservice.enums.GoldPurity;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rahim Ahmed
 * @created 02/12/2023
 */
@Getter
@Setter
public class GoldPriceCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCalculator.class);
    private double pricePerGram;
    private BigDecimal pricePerOunce;

    public void calculatePricePerGram() {
        try {
            double GRAMS_PER_TROY_OUNCE = 31.1035;
            BigDecimal ouncesBigDecimal = pricePerOunce.setScale(2, RoundingMode.HALF_UP);
            BigDecimal gramsPerOunce = new BigDecimal(GRAMS_PER_TROY_OUNCE);

            BigDecimal result = ouncesBigDecimal.divide(gramsPerOunce, 2, RoundingMode.HALF_UP);
            pricePerGram = result.doubleValue();

            LOG.info("Conversion successful: £{} per ounce is £{} per gram", ouncesBigDecimal, pricePerGram);

        } catch (ArithmeticException ex) {
            LOG.error("Error converting price per ounce to per gram: {}", ex.getMessage());
            throw new RuntimeException("Error converting price per ounce to per gram", ex);
        }
    }

    public double calculateGoldPriceByCarat(String carat) {
        try {
            double purity = GoldPurity.getPurityByCarat(carat);
            BigDecimal result = BigDecimal.valueOf(purity * pricePerGram).setScale(2, RoundingMode.HALF_UP);
            return result.doubleValue();
        } catch (IllegalArgumentException e) {
            LOG.error("Error calculating gold price for carat {}: {}", carat, e.getMessage());
            return 0;
        }
    }

    public BigDecimal calculateGoldPrice(String caratPurity, BigDecimal netWeight) {
        try {
            if (caratPurity.isEmpty() || (netWeight != null && netWeight.compareTo(BigDecimal.ZERO) < 0) || pricePerGram < 0) {
                LOG.error("Invalid input values. Carat purity: {}, Net weight: {}, Price per gram: {}", caratPurity, netWeight, pricePerGram);
                throw new IllegalArgumentException("Invalid input values. Carat purity, net weight (if provided), and price per gram must be non-negative.");
            }

            BigDecimal caratPrice = BigDecimal.valueOf(calculateGoldPriceByCarat(caratPurity));
            BigDecimal goldPrice;

            if (netWeight == null || netWeight.compareTo(BigDecimal.ZERO) == 0) {
                goldPrice = caratPrice.multiply(BigDecimal.ONE);
            } else {
                goldPrice = caratPrice.multiply(netWeight);
            }

            goldPrice = goldPrice.setScale(2, RoundingMode.HALF_UP);

            LOG.info("Calculated Gold Price: Carat Purity: {}, Net Weight: {} grams, Price per gram: £{}, Total Price: £{}", caratPurity, netWeight, pricePerGram, goldPrice);

            return goldPrice;
        } catch (IllegalArgumentException e) {
            LOG.error("Error calculating gold price: {}", e.getMessage(), e);
            throw e;
        }
    }
}