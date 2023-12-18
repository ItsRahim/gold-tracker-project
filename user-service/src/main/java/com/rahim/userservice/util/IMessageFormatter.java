package com.rahim.userservice.util;

import java.util.Map;

public interface IMessageFormatter {
    void updateMapKey(Map<String, Object> map, String oldKey, String newKey);
    void formatInstant(Map<String, Object> map, String key);
}
