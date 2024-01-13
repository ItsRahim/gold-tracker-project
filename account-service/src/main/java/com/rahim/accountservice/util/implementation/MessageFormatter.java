package com.rahim.accountservice.util.implementation;

import com.rahim.accountservice.util.IMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class MessageFormatter implements IMessageFormatter {
    private static final Logger LOG = LoggerFactory.getLogger(MessageFormatter.class);
    @Override
    public void updateMapKey(Map<String, Object> map, String oldKey, String newKey) {
        if(map.containsKey(oldKey)) {
            Object value = map.remove(oldKey);

            map.put(newKey, value);
        }
    }

    @Override
    public void formatInstant(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            Object value = map.get(key);
            if (value != null) {
                if (value instanceof Instant instant) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
                    String formattedDate = formatter.format(instant);
                    map.put(key, formattedDate);
                } else if (value instanceof Date sqlDate) {
                    LocalDate localDate = sqlDate.toLocalDate();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = formatter.format(localDate);
                    map.put(key, formattedDate);
                } else {
                    LOG.error("Unsupported type for key {}: {}", key, value.getClass().getName());
                }
            } else {
                LOG.error("Value for key {} is null", key);
            }
        }
    }
}
