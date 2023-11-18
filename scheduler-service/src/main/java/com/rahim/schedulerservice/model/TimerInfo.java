package com.rahim.schedulerservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TimerInfo {
    private int totalFireCount;
    private boolean runForever;
    private long repeatIntervalMs;
    private long initialOffsetMs;
    private String callbackData;
}
