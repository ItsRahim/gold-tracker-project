package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.IGoldTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gold/pricing-service/gold-type")
public class GoldTypeController {
    private final IGoldTypeService goldTypeService;
    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeController.class);

    @GetMapping
    public ResponseEntity<List<GoldType>> getAllGoldTypes() {
        List<GoldType> goldTypes = goldTypeService.getAllGoldTypes();
        return ResponseEntity.status(HttpStatus.OK).body(goldTypes);
    }

    @PostMapping
    public ResponseEntity<Void> addGoldType(@RequestBody GoldType goldType) {
        try {
            goldTypeService.addGoldType(goldType);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error adding gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{goldId}")
    public ResponseEntity<Void> updateGoldType(@PathVariable int goldId, @RequestBody Map<String, String> updatedData) {
        try {
            goldTypeService.updateGoldType(goldId, updatedData);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOG.error("Error updating gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{goldId}")
    public ResponseEntity<Void> deleteGoldType(@PathVariable int goldId) {
        try {
            goldTypeService.deleteGoldType(goldId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOG.error("Error deleting gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
