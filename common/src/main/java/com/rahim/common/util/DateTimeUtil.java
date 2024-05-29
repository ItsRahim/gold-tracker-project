package com.rahim.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Rahim Ahmed
 * @created 25/05/2024
 */
public class DateTimeUtil {

    private DateTimeUtil() {}

    public static Instant generateInstant() {
        return Instant.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public static String formatInstantDate(Instant instant) {
        if (instant == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);

        return formatter
                .format(instant)
                .replace("T", " ")
                .replace("Z", "");
    }

    public static String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

    public static String getFormattedTime() {
        return generateInstant()
                .toString()
                .replace("T", " ");
    }
}
