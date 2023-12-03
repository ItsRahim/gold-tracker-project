package com.rahim.schedulerservice.controller;

import com.rahim.schedulerservice.model.TimerInfo;
import com.rahim.schedulerservice.service.ISchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gold/scheduler-service/jobs")
@RequiredArgsConstructor
public class SchedulerController {

    private final ISchedulerService schedulerService;

    @GetMapping
    public ResponseEntity<List<TimerInfo>> getAllRunningTimers() {
        List<TimerInfo> runningTimers = schedulerService.getAllRunningTimers();
        return ResponseEntity.status(HttpStatus.OK).body(runningTimers);
    }

    @GetMapping("/{timerId}")
    public ResponseEntity<TimerInfo> getRunningTimer(@PathVariable String timerId) {
        TimerInfo runningTimer = schedulerService.getRunningTimer(timerId);

        if (runningTimer != null) {
            return ResponseEntity.status(HttpStatus.OK).body(runningTimer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{timerId}")
    public ResponseEntity<Boolean> deleteTimer(@PathVariable String timerId) {
        Boolean deletionStatus = schedulerService.deleteTimer(timerId);

        if (deletionStatus != null && deletionStatus) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}