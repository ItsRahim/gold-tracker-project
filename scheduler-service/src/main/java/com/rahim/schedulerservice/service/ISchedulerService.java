package com.rahim.schedulerservice.service;

import com.rahim.schedulerservice.model.TimerInfo;


public interface ISchedulerService {
    void schedule(final Class clazz, final TimerInfo info);
}
