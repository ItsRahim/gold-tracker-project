package com.rahim.accountservice.util.implementation;

import com.rahim.accountservice.util.IMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
@Component
public class MessageFormatter implements IMessageFormatter {

    private static final Logger LOG = LoggerFactory.getLogger(MessageFormatter.class);

    @Override
    public void formatInstant(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value != null) {
            if (value instanceof Instant instant) {
                String formattedDate = formatInstantDate(instant);
                map.put(key, formattedDate);
            } else if (value instanceof Date sqlDate) {
                String formattedDate = formatDate(sqlDate.toLocalDate());
                map.put(key, formattedDate);
            } else {
                LOG.error("Unsupported type for key {}: {}", key, value.getClass().getName());
            }
        } else {
            LOG.error("Value for key {} is null", key);
        }
    }

    private String formatInstantDate(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);

        return formatter.format(instant);
    }

    private String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

}
