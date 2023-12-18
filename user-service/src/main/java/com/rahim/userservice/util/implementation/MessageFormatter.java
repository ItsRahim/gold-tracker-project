package com.rahim.userservice.util.implementation;

import com.rahim.userservice.util.IMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneOffset;
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
                Instant instant = (Instant) value;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
                try {
                    String formattedDate = formatter.format(instant);
                    map.put(key, formattedDate);
                } catch (DateTimeException e) {
                    LOG.error("Error formatting Instant value for key {}: {}", key, e.getMessage(), e);
                }
            }
        }
    }
}
