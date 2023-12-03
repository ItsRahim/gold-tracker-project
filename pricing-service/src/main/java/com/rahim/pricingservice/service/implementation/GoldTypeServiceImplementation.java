package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldTypeRepository;
import com.rahim.pricingservice.service.IGoldPriceService;
import com.rahim.pricingservice.service.IGoldTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldTypeServiceImplementation implements IGoldTypeService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeServiceImplementation.class);
    private final GoldTypeRepository goldTypeRepository;
    private final IGoldPriceService goldPriceService;

    @Override
    public List<Integer> getAllIds() {
        try {
            List<GoldType> goldTypes = goldTypeRepository.findAll();

            List<Integer> ids = goldTypes.stream()
                    .map(GoldType::getId)
                    .collect(Collectors.toList());

            LOG.info("Retrieved all GoldType IDs: {}", ids);
            return ids;
        } catch (Exception e) {
            LOG.error("Error getting all GoldType IDs: {}", e.getMessage(), e);
            throw new RuntimeException("Error getting all GoldType IDs", e);
        }
    }

    @Override
    @Transactional
    public void addGoldType(GoldType goldType) throws Exception {
        try {
            GoldType savedGoldType = goldTypeRepository.save(goldType);
            LOG.info("Successfully added new gold type: {}", savedGoldType.getName());
            goldPriceService.processNewGoldType(savedGoldType.getId());
        } catch (Exception e) {
            LOG.error("Unexpected error adding new gold type: {}", e.getMessage());
            throw new Exception("Unexpected error adding new gold type", e);
        }
    }

    @Override
    @Transactional
    public void updateGoldType(int goldId, Map<String, String> updatedData) throws Exception {
        try {
            if (!existsById(goldId)) {
                LOG.warn("GoldType with ID {} not found. Unable to update.", goldId);
                return;
            }

            GoldType existingGoldType = goldTypeRepository.findById(goldId).orElseThrow(
                    () -> new IllegalArgumentException("GoldType with ID " + goldId + " not found"));

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

            goldTypeRepository.save(existingGoldType);

            LOG.info("Successfully updated gold type with ID {}: {}", goldId, existingGoldType);
        } catch (Exception e) {
            LOG.error("Unexpected error updating gold type: {}", e.getMessage());
            throw new Exception("Unexpected error updating gold type", e);
        }
    }

    @Override
    public void deleteGoldType(int goldId) {
        try {
            if (!existsById(goldId)) {
                LOG.warn("Gold type with ID: {} does not exist. Unable to delete.", goldId);
                return;
            }
            goldPriceService.deleteGoldType(goldId);
            goldTypeRepository.deleteById(goldId);
            LOG.info("Gold type with ID {} deleted successfully.", goldId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GoldType> getAllGoldTypes() {
        List<GoldType> goldTypes = goldTypeRepository.findAll();

        if (!goldTypes.isEmpty()) {
            LOG.info("Found {} users in the database", goldTypes.size());
        } else {
            LOG.info("No users found in the database");
        }

        return goldTypes;
    }

    private boolean existsById(int goldId) {
        return goldTypeRepository.existsById(goldId);
    }
}
