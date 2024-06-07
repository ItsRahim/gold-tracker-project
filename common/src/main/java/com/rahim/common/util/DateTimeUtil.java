package com.rahim.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Utility class for handling date and time operations.
 * This class cannot be instantiated.
 *
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DateTimeUtil {

    // Prevent instantiation
    private DateTimeUtil() {}

    private static final DateTimeFormatter INSTANT_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static Instant generateInstant() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static String formatInstantDate(Instant instant) {
        return Optional.ofNullable(instant)
                .map(INSTANT_FORMATTER::format)
                .orElse("");
    }

    public static String formatDate(LocalDate localDate) {
        return DATE_FORMATTER.format(localDate);
    }

    public static String getFormattedTime() {
        return INSTANT_FORMATTER.format(generateInstant());
    }

    public static LocalDate getLocalDate() {
        return LocalDate.now(ZoneId.of("UTC"));
    }
}
