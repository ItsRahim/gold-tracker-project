package com.rahim.serviceregistry.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerInfo {
    private int totalFireCount;
    private boolean runForever;
    private long repeatIntervalMs;
    private long initialOffsetMs;
    private String callbackData;
}
