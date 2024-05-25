package com.rahim.notificationservice.service.threshold;

import com.rahim.notificationservice.model.ThresholdAlert;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
public interface IThresholdCreationService {

    void createNotification(ThresholdAlert thresholdAlert);
}
