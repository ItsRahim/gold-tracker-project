package com.rahim.schedulerservice.controller;

import com.rahim.schedulerservice.model.TimerInfo;
import com.rahim.schedulerservice.service.ISchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gold/jobs")
@RequiredArgsConstructor
public class SchedulerController {

    private final ISchedulerService schedulerService;

    @GetMapping
    public List<TimerInfo> getAllRunningTimers() {
        return schedulerService.getAllRunningTimers();
    }

    @GetMapping("/{timerId}")
    public TimerInfo getRunningTimer(@PathVariable String timerId) {
        return schedulerService.getRunningTimer(timerId);
    }

    @DeleteMapping("/{timerId}")
    public Boolean deleteTimer(@PathVariable String timerId) {
        return schedulerService.deleteTimer(timerId);
    }
}
