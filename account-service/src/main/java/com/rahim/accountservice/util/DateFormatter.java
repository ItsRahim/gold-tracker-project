package com.rahim.accountservice.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public class DateFormatter {

    private static DateFormatter dateFormatter = null;

    public static DateFormatter getInstance() {
        if (dateFormatter == null) {
            dateFormatter = new DateFormatter();
        }

        return dateFormatter;
    }

    public String formatInstantDate(Instant instant) {
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

    public String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

}
