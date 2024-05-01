package com.rahim.notificationservice.controller;

import com.rahim.notificationservice.model.ThresholdAlert;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdCreationService;
import com.rahim.notificationservice.service.threshold.IThresholdDeletionService;
import com.rahim.notificationservice.service.threshold.IThresholdUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rahim.notificationservice.constants.ThresholdControllerEndpoint.*;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
public class ThresholdController {

    private static final Logger LOG = LoggerFactory.getLogger(ThresholdController.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final IThresholdCreationService thresholdCreationService;
    private final IThresholdDeletionService thresholdDeletionService;
    private final IThresholdUpdateService thresholdUpdateService;

    @GetMapping(THRESHOLD_ID)
    public ResponseEntity<ThresholdAlert> getAlertById(@PathVariable int thresholdId) {
        LOG.info("Fetching alert by ID: {}", thresholdId);
        Optional<ThresholdAlert> thresholdAlert = thresholdAlertRepositoryHandler.findById(thresholdId);
        if (thresholdAlert.isPresent()) {
            ThresholdAlert alert = thresholdAlert.get();
            return ResponseEntity.status(HttpStatus.OK).body(alert);
        } else {
            LOG.warn("Alert with ID {} not found", thresholdId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThresholdAlert());
        }
    }

    @GetMapping()
    public ResponseEntity<List<ThresholdAlert>> getAlerts() {
        LOG.info("Fetching all alerts");
        List<ThresholdAlert> thresholdAlerts = thresholdAlertRepositoryHandler.getAllActiveAlerts();
        return ResponseEntity.status(HttpStatus.OK).body(thresholdAlerts);
    }

    @GetMapping(ACCOUNT_ID)
    public ResponseEntity<ThresholdAlert> getAlertByAccountId(@PathVariable int accountId) {
        LOG.info("Fetching alert by Account ID: {}", accountId);
        Optional<ThresholdAlert> thresholdAlertOptional = thresholdAlertRepositoryHandler.getAlertByAccountId(accountId);
        if (thresholdAlertOptional.isPresent()) {
            LOG.info("Alert found for Account ID: {}", accountId);
            return ResponseEntity.status(HttpStatus.OK).body(thresholdAlertOptional.get());
        } else {
            LOG.warn("Alert not found for Account ID: {}", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ThresholdAlert());
        }
    }

    @PostMapping()
    public ResponseEntity<String> createThreshold(@RequestBody ThresholdAlert request) {
        try {
            if (thresholdCreationService.createNotification(request)) {
                LOG.info("Threshold created successfully for account ID: {}", request.getAccountId());
                return ResponseEntity.status(HttpStatus.CREATED).body("Threshold created successfully");
            } else {
                LOG.warn("Notification not created for account ID: {}. Account invalid/notifications not enabled on account", request.getAccountId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Notification not created. Account invalid/notifications not enabled on account");
            }
        } catch (Exception e) {
            LOG.error("Error creating threshold: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating threshold: " + e.getMessage());
        }
    }

    @PutMapping(THRESHOLD_ID)
    public ResponseEntity<String> updateThreshold(@PathVariable int thresholdId, @RequestBody Map<String, String> updatedData) {
        try {
            if (thresholdUpdateService.updateNotification(thresholdId, updatedData)) {
                LOG.info("Threshold successfully updated for ID: {}", thresholdId);
                return ResponseEntity.status(HttpStatus.OK).body("Threshold successfully updated");
            } else {
                LOG.warn("Failed to update threshold for ID: {}. Threshold not found or update unsuccessful", thresholdId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Threshold not found or update unsuccessful");
            }
        } catch (Exception e) {
            LOG.error("Error updating threshold for ID {}: {}", thresholdId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating threshold: " + e.getMessage());
        }
    }

    @DeleteMapping(THRESHOLD_ID)
    public ResponseEntity<String> deleteThreshold(@PathVariable int thresholdId) {
        try {
            if (thresholdDeletionService.deleteNotification(thresholdId)) {
                LOG.info("Threshold successfully deleted for ID: {}", thresholdId);
                return ResponseEntity.status(HttpStatus.OK).body("Threshold successfully deleted");
            } else {
                LOG.warn("Failed to delete threshold for ID: {}. Threshold not found or deletion unsuccessful", thresholdId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Threshold not found or deletion unsuccessful");
            }
        } catch (Exception e) {
            LOG.error("Error deleting threshold for ID {}: {}", thresholdId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting threshold: " + e.getMessage());
        }
    }
}
