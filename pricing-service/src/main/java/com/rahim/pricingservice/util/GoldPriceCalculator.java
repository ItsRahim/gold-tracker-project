package com.rahim.pricingservice.util;

import com.rahim.pricingservice.enums.GoldPurity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rahim Ahmed
 * @created 02/12/2023
 */
@Configuration
public class GoldPriceCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCalculator.class);
    private static final BigDecimal GRAMS_PER_OUNCE = BigDecimal.valueOf(31.1035);
    private static final int SCALE = 2;

    @Getter
    private static BigDecimal pricePerGram;

    /**
     * Calculates the price per gram based on the given price per ounce.
     *
     * @param pricePerOunce The price per ounce (in GBP).
     * @throws RuntimeException If an error occurs during the conversion.
     */
    public static void calculatePricePerGram(BigDecimal pricePerOunce) {
        try {
            BigDecimal ouncesBigDecimal = pricePerOunce.setScale(SCALE, RoundingMode.HALF_UP);
            pricePerGram = ouncesBigDecimal.divide(GRAMS_PER_OUNCE, SCALE, RoundingMode.HALF_UP);

            LOG.debug("Conversion successful: £{} per ounce is £{} per gram", ouncesBigDecimal, pricePerGram);
        } catch (ArithmeticException ex) {
            LOG.error("Error converting price per ounce to per gram: {}", ex.getMessage());
            throw new RuntimeException("Error converting price per ounce to per gram", ex);
        }
    }

    /**
     * Calculates the gold price based on the given carat purity.
     *
     * @param carat The carat purity (e.g., "24K", "18K").
     * @return The gold price as a {@link BigDecimal}.
     * @throws RuntimeException If an error occurs during the calculation.
     */
    private BigDecimal calculateGoldPriceByCarat(String carat) {
        try {
            BigDecimal caratPurity = BigDecimal.valueOf(GoldPurity.getPurityByCarat(carat));

            return caratPurity.multiply(pricePerGram).setScale(SCALE, RoundingMode.HALF_UP);
        } catch (IllegalArgumentException e) {
            LOG.error("Error calculating gold price for carat {}: {}", carat, e.getMessage());
            throw new RuntimeException("Error calculating gold price", e);
        }
    }

    /**
     * Calculates the gold price based on the given carat purity and net weight.
     *
     * @param caratPurity The carat purity (e.g., "24K", "18K").
     * @param netWeight The net weight of gold in grams (can be null if not provided).
     * @return The calculated gold price as a {@link BigDecimal}.
     * @throws IllegalArgumentException If input values are invalid (negative price per gram, empty carat purity, or negative net weight).
     */
    public BigDecimal calculateGoldPrice(String caratPurity, BigDecimal netWeight) {
        try {
            validateInput(caratPurity, netWeight);

            BigDecimal goldPriceByCarat = calculateGoldPriceByCarat(caratPurity);
            BigDecimal totalGoldPrice;

            if (netWeight == null || netWeight.compareTo(BigDecimal.ZERO) == 0) {
                totalGoldPrice = goldPriceByCarat.multiply(BigDecimal.ONE);
            } else {
                totalGoldPrice = goldPriceByCarat.multiply(netWeight);
            }

            totalGoldPrice = totalGoldPrice.setScale(SCALE, RoundingMode.HALF_UP);

            LOG.debug("Calculated Gold Price: Carat Purity: {}, Net Weight: {} grams, Price per gram: £{}, Total Price: £{}",
                    caratPurity, netWeight, pricePerGram, totalGoldPrice);

            return totalGoldPrice;
        } catch (IllegalArgumentException e) {
            LOG.error("Error calculating gold price: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Validates the input values for carat purity and net weight.
     *
     * @param caratPurity The carat purity (e.g., "24K", "18K").
     * @param netWeight   The net weight (in grams).
     * @throws IllegalArgumentException If any input value is invalid.
     */
    private void validateInput(String caratPurity, BigDecimal netWeight) {
        if (caratPurity.isEmpty() || (netWeight != null && netWeight.compareTo(BigDecimal.ZERO) < 0) || pricePerGram.compareTo(BigDecimal.ZERO) < 0) {
            LOG.error("Invalid input values. Carat purity: {}, Net weight: {}, Price per gram: {}", caratPurity, netWeight, pricePerGram);
            throw new IllegalArgumentException("Invalid input values. Carat purity, net weight (if provided), and price per gram must be non-negative.");
        }
    }

}