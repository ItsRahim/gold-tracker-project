package com.rahim.pricingservice.service.type.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldTypeUpdateService implements IGoldTypeUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeUpdateService.class);

    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;

    @Override
    public void updateGoldType(int goldId, Map<String, String> updatedData) {
        try {
            GoldType existingGoldType = goldTypeRepositoryHandler.findById(goldId)
                    .orElseThrow(() -> new IllegalArgumentException("GoldType with ID " + goldId + " not found"));

            updatedData.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        existingGoldType.setName(value);
                        break;
                    case "netWeight":
                        existingGoldType.setNetWeight(new BigDecimal(value));
                        break;
                    case "carat":
                        existingGoldType.setCarat(value);
                        break;
                    case "description":
                        existingGoldType.setDescription(value);
                        break;
                    default:
                        LOG.warn("Ignoring unknown field: {}", key);
                }
            });

            goldTypeRepositoryHandler.saveGoldType(existingGoldType);

            LOG.info("Successfully updated gold type with ID {}: {}", goldId, existingGoldType);
        } catch (Exception e) {
            LOG.error("Error updating gold type: {}", e.getMessage());
            throw new RuntimeException("Failed to update gold type.", e);
        }
    }
}
