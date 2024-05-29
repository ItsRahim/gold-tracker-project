package com.rahim.investmentservice.util;

import com.rahim.common.exception.ValidationException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Rahim Ahmed
 * @created 28/05/2024
 */
public class ProfitLossUtil {

    public static BigDecimal calculateProfitLossPercentage(BigDecimal initialValue, BigDecimal currentValue) {
        if (initialValue == null || currentValue == null || initialValue.compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("Total purchase amount must not be null or zero, and current value must not be null");
        }

        BigDecimal difference = currentValue.subtract(initialValue);

        BigDecimal percentage = difference
                .divide(initialValue, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return percentage.setScale(2, RoundingMode.HALF_UP);
    }
}
