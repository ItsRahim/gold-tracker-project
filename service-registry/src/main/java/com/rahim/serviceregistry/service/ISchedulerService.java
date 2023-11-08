package com.rahim.serviceregistry.service;

import com.rahim.userservice.scheduler.model.TimerInfo;

import java.util.List;

public interface ISchedulerService {
    void schedule(final Class clazz, final TimerInfo info);
    List<TimerInfo> getAllRunningTimers();
    TimerInfo getRunningTimer(String timerId);
}
