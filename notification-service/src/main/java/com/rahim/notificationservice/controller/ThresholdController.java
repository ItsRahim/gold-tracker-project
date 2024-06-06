package com.rahim.notificationservice.controller;

import com.rahim.notificationservice.entity.ThresholdAlert;
import com.rahim.notificationservice.service.repository.IThresholdAlertRepositoryHandler;
import com.rahim.notificationservice.service.threshold.IThresholdCreationService;
import com.rahim.notificationservice.service.threshold.IThresholdDeletionService;
import com.rahim.notificationservice.service.threshold.IThresholdUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.rahim.notificationservice.constants.ThresholdControllerEndpoint.*;

/**
 * @author Rahim Ahmed
 * @created 01/05/2024
 */
@RestController
@RequestMapping(BASE_URL)
@RequiredArgsConstructor
@Tag(name = "Threshold Notification Management", description = "Endpoints for managing user gold price notifications")
public class ThresholdController {

    private static final Logger log = LoggerFactory.getLogger(ThresholdController.class);
    private final IThresholdAlertRepositoryHandler thresholdAlertRepositoryHandler;
    private final IThresholdCreationService thresholdCreationService;
    private final IThresholdDeletionService thresholdDeletionService;
    private final IThresholdUpdateService thresholdUpdateService;

    @Operation(summary = "Get price alert by threshold id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threshold alert found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThresholdAlert.class))),
            @ApiResponse(responseCode = "404", description = "Threshold alert not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = THRESHOLD_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThresholdAlert> getAlertById(
            @Parameter(description = "ID of the alert to fetch", required = true) @PathVariable int thresholdId) {
        ThresholdAlert thresholdAlert = thresholdAlertRepositoryHandler.findById(thresholdId);
        return ResponseEntity.status(HttpStatus.OK).body(thresholdAlert);
    }

    @Operation(summary = "Get all alerts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threshold alerts found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThresholdAlert.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving all alerts", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ThresholdAlert>> getAlerts() {
        List<ThresholdAlert> thresholdAlerts = thresholdAlertRepositoryHandler.getAllActiveAlerts();
        return ResponseEntity.status(HttpStatus.OK).body(thresholdAlerts);
    }

    @Operation(summary = "Get alert by Account ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threshold alert found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThresholdAlert.class))),
            @ApiResponse(responseCode = "404", description = "Alert not found for the given Account ID", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json"))
    })
    @GetMapping(value = ACCOUNT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ThresholdAlert> getAlertByAccountId(
            @Parameter(description = "ID of the account to fetch the alert for", required = true) @PathVariable int accountId) {
        ThresholdAlert thresholdAlert = thresholdAlertRepositoryHandler.getAlertByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(thresholdAlert);
    }

    @Operation(summary = "Create a new threshold alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Threshold created successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Notification not created. Account invalid/notifications not enabled on account", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error creating threshold", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<String> createThreshold(
            @Parameter(description = "Threshold alert details", required = true) @RequestBody ThresholdAlert request) {
        thresholdCreationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Threshold created successfully");
    }

    @Operation(summary = "Update an existing threshold alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threshold successfully updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Threshold not found or update unsuccessful", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error updating threshold", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(THRESHOLD_ID)
    public ResponseEntity<String> updateThreshold(
            @Parameter(description = "ID of the threshold to update", required = true) @PathVariable int thresholdId,
            @Parameter(description = "Updated data for the threshold", required = true) @RequestBody Map<String, String> updatedData) {
        thresholdUpdateService.updateNotification(thresholdId, updatedData);
        return ResponseEntity.status(HttpStatus.OK).body("Threshold successfully updated");
    }

    @Operation(summary = "Delete an existing threshold alert")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Threshold successfully deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Threshold not found or deletion unsuccessful", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error deleting threshold", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = THRESHOLD_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteThreshold(
            @Parameter(description = "ID of the threshold to delete", required = true) @PathVariable int thresholdId) {
        thresholdDeletionService.deleteNotification(thresholdId);
        return ResponseEntity.status(HttpStatus.OK).body("Threshold successfully deleted");
    }
}
