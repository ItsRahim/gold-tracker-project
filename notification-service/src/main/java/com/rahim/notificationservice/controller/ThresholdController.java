package com.rahim.notificationservice.controller;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.IThresholdService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rahim.notificationservice.constants.ThresholdControllerEndpoint.BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class ThresholdController {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdController.class);
    private final IThresholdService thresholdService;

    @PostMapping()
    public ResponseEntity<String> createThreshold(@RequestBody ThresholdAlert request) {
        try {
            thresholdService.createNotification(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Threshold created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating threshold: " + e.getMessage());
        }
    }
}
