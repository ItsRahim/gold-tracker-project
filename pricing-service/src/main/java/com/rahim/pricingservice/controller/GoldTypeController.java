package com.rahim.pricingservice.controller;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.type.IGoldTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
public class GoldTypeController {
    private final IGoldTypeService goldTypeService;
    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeController.class);

    @GetMapping
    public ResponseEntity<List<GoldType>> getAllGoldTypes() {
        List<GoldType> goldTypes = goldTypeService.getAllGoldTypes();
        return ResponseEntity.status(HttpStatus.OK).body(goldTypes);
    }

    @GetMapping(GOLD_TYPE_ID)
    public ResponseEntity<GoldType> getGoldTypeById(@PathVariable int goldTypeId) {
        try {
            Optional<GoldType> goldTypeOptional = goldTypeService.findById(goldTypeId);

            return goldTypeOptional.map(goldType -> ResponseEntity.status(HttpStatus.OK).body(goldType))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Void> addGoldType(@RequestBody GoldType goldType) {
        try {
            goldTypeService.addGoldType(goldType);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOG.error("Error adding gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(GOLD_TYPE_ID)
    public ResponseEntity<Void> updateGoldType(@PathVariable int goldTypeId, @RequestBody Map<String, String> updatedData) {
        try {
            goldTypeService.updateGoldType(goldTypeId, updatedData);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOG.error("Error updating gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping(GOLD_TYPE_ID)
    public ResponseEntity<Void> deleteGoldType(@PathVariable int goldTypeId) {
        try {
            goldTypeService.deleteGoldType(goldTypeId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOG.error("Error deleting gold type: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
