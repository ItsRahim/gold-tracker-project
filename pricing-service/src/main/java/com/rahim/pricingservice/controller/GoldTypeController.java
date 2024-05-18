package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeCreationService;
import com.rahim.pricingservice.service.type.IGoldTypeDeletionService;
import com.rahim.pricingservice.service.type.IGoldTypeUpdateService;
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
import java.util.Optional;

import static com.rahim.pricingservice.constant.GoldTypeURLConstant.GOLD_TYPE_ID;
import static com.rahim.pricingservice.constant.GoldTypeURLConstant.TYPE_BASE_URL;

/**
 * @author Rahim Ahmed
 * @created 03/12/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(TYPE_BASE_URL)
@Tag(name = "Gold Type Management", description = "Endpoints for managing gold types")
public class GoldTypeController {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeController.class);
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;
    private final IGoldTypeCreationService goldTypeCreationService;
    private final IGoldTypeUpdateService goldTypeUpdateService;
    private final IGoldTypeDeletionService goldTypeDeletionService;

    @Operation(summary = "Get all gold types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gold types retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoldType.class))),
            @ApiResponse(responseCode = "500", description = "Error retrieving gold types", content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GoldType>> getAllGoldTypes() {
        try {
            List<GoldType> goldTypes = goldTypeRepositoryHandler.getAllGoldTypes();
            return ResponseEntity.status(HttpStatus.OK).body(goldTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get gold type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gold type found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoldType.class))),
            @ApiResponse(responseCode = "404", description = "Gold type not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error retrieving gold type", content = @Content)
    })
    @GetMapping(value = GOLD_TYPE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GoldType> getGoldTypeById(
            @Parameter(description = "ID of the gold type to be retrieved", required = true) @PathVariable int goldTypeId) {
        try {
            Optional<GoldType> goldTypeOptional = goldTypeRepositoryHandler.findById(goldTypeId);

            return goldTypeOptional.map(goldType -> ResponseEntity.status(HttpStatus.OK).body(goldType))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GoldType()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Add a new gold type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Gold type created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoldType.class))),
            @ApiResponse(responseCode = "409", description = "Gold type already exists", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error creating gold type", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addGoldType(
            @Parameter(description = "The details of the gold type to be added", required = true) @RequestBody GoldType goldType) {
        try {
            String goldTypeName = goldType.getName();
            if (goldTypeRepositoryHandler.existsByName(goldTypeName)){
                LOG.warn("{}, already exists. Cannot create duplicate", goldTypeName);
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot create duplicate gold type.");
            } else {
                goldTypeCreationService.addGoldType(goldType);
                return ResponseEntity.status(HttpStatus.CREATED).body(goldType);
            }
        } catch (Exception e) {
            LOG.error("Error adding gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Update a gold type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gold type updated successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Gold type not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error updating gold type", content = @Content(mediaType = "application/json"))
    })
    @PutMapping(value = GOLD_TYPE_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateGoldType(
            @Parameter(description = "ID of the gold type to be updated", required = true) @PathVariable int goldTypeId,
            @Parameter(description = "Map of updated gold type data", required = true) @RequestBody Map<String, String> updatedData) {
        try {
            if (goldTypeRepositoryHandler.existsById(goldTypeId)) {
                goldTypeUpdateService.updateGoldType(goldTypeId, updatedData);
                return ResponseEntity.status(HttpStatus.OK).body("Gold Type with ID " + goldTypeId + " updated.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gold Type with ID " + goldTypeId + " not found.");
            }
        } catch (Exception e) {
            LOG.error("Error updating gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Delete a gold type by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Gold type deleted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Gold type not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error deleting gold type", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping(value = GOLD_TYPE_ID, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteGoldType(
            @Parameter(description = "ID of the gold type to be deleted", required = true) @PathVariable int goldTypeId) {
        try {
            if (goldTypeRepositoryHandler.existsById(goldTypeId)) {
                LOG.info("Gold Type with ID {} found. Attempting to delete...", goldTypeId);
                goldTypeDeletionService.deleteGoldType(goldTypeId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gold Type with ID " + goldTypeId + " not found.");
            }
        } catch (Exception e) {
            LOG.error("Error deleting gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
