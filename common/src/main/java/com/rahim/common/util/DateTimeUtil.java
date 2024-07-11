package com.rahim.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for handling date and time operations.
 * This class cannot be instantiated.
 *
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DateTimeUtil {

    private DateTimeUtil() {
    }

    private static final DateTimeFormatter INSTANT_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Instant generateInstant() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static String getFormattedDate(LocalDate localDate) {
        return DATE_FORMATTER.format(localDate);
    }

    public static String getFormattedInstant() {
        return INSTANT_FORMATTER.format(generateInstant());
    }

    public static LocalDate getLocalDate() {
        return LocalDate.now(ZoneId.of("UTC"));
    }
}
