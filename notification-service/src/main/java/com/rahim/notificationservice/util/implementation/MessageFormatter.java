package com.rahim.notificationservice.util.implementation;

import com.rahim.notificationservice.util.IMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
}
