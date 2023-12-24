package com.rahim.notificationservice.util;

import java.util.Map;

public interface IMessageFormatter {
    void updateMapKey(Map<String, Object> map, String oldKey, String newKey);
}
