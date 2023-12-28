package com.rahim.notificationservice.service;

import com.rahim.notificationservice.model.ThresholdAlert;


public interface IUserExistenceChecker {
    boolean checkUserExistence(String userId, ThresholdAlert thresholdAlert);
}
