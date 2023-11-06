package com.rahim.userservice.quartz.service;

import com.rahim.userservice.quartz.model.TimerInfo;

public interface ISchedulerService {
    void schedule(final Class clazz, final TimerInfo info);
}
