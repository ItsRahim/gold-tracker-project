package com.rahim.common.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DateTimeGenerator {

    private DateTimeGenerator () {}

    public static Instant generateInstant() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static String getFormattedTime() {
        return generateInstant()
                .toString()
                .replace("T", " ");
    }
}
