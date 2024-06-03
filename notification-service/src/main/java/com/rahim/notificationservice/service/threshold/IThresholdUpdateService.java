package com.rahim.notificationservice.service.threshold;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
public interface IThresholdUpdateService {

    void updateNotification(int thresholdId, Map<String, String> updatedData);
}
