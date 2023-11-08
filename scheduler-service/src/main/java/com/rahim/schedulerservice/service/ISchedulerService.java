package com.rahim.schedulerservice.service;

import com.rahim.schedulerservice.model.TimerInfo;

import java.util.List;

public interface ISchedulerService {
    void schedule(final Class clazz, final TimerInfo info);
    List<TimerInfo> getAllRunningTimers();
    TimerInfo getRunningTimer(String timerId);
}
