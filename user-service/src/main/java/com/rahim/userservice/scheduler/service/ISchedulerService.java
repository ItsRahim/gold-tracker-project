package com.rahim.userservice.scheduler.service;

import com.rahim.userservice.scheduler.model.TimerInfo;

public interface ISchedulerService {
    void schedule(final Class clazz, final TimerInfo info);
}
