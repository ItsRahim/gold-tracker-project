package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.constant.TopicConstants;
import com.rahim.pricingservice.kafka.IKafkaService;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldTypeRepository;
import com.rahim.pricingservice.service.IGoldTypeService;
import com.rahim.pricingservice.service.price.IGoldPriceCreationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldTypeService implements IGoldTypeService {
    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeService.class);
    private final GoldTypeRepository goldTypeRepository;
    private final IKafkaService kafkaService;
    private final IGoldPriceCreationService goldPriceCreationService;
    private final TopicConstants topicConstants;


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
            String goldName = goldType.getName();

            if(goldTypeExists(goldName)) {
                LOG.warn("Gold type with name {} already exists. Not creating duplicate.", goldType.getName());
            } else {
                boolean anyNull = ObjectUtils.anyNull(goldType);
                if(!anyNull) {
                    GoldType savedGoldType = goldTypeRepository.save(goldType);
                    LOG.info("Successfully added new gold type: {}", savedGoldType.getName());
                    goldPriceCreationService.processNewGoldType(savedGoldType);
                } else {
                    LOG.warn("Given gold types has one or more null values. Not adding to database");
                }
            }
        } catch (Exception e) {
            LOG.error("Unexpected error adding new gold type: {}", e.getMessage());
            throw new Exception("Unexpected error adding new gold type", e);
        }
    }

    private boolean goldTypeExists(String name) {
        return goldTypeRepository.existsByName(name);
    }

    @Override
    public void updateGoldType(int goldId, Map<String, String> updatedData) {
        try {
            GoldType existingGoldType = goldTypeRepository.findById(goldId)
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

            goldTypeRepository.save(existingGoldType);

            LOG.info("Successfully updated gold type with ID {}: {}", goldId, existingGoldType);
        } catch (Exception e) {
            LOG.error("Error updating gold type: {}", e.getMessage());
            throw new RuntimeException("Failed to update gold type.", e);
        }
    }

    @Override
    public void deleteGoldType(int goldId) {
        try {
            if (!existsById(goldId)) {
                LOG.warn("Gold type with ID: {} does not exist. Unable to delete.", goldId);
            }
            kafkaService.sendMessage(topicConstants.getDeleteGoldTypeTopic(), String.valueOf(goldId));
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
            LOG.info("Found {} gold types in the database", goldTypes.size());
        } else {
            LOG.info("No gold types found in the database");
        }

        return goldTypes;
    }

    @Override
    public Optional<GoldType> findById(int goldId) {
        try {
            Optional<GoldType> optionalGoldType = goldTypeRepository.findById(goldId);

            if (optionalGoldType.isPresent()) {
                GoldType goldType = optionalGoldType.get();
                LOG.info("Gold type with ID {} found: {}", goldId, goldType);
                return Optional.of(goldType);
            } else {
                LOG.warn("Gold type with ID {} not found", goldId);
                return Optional.empty();
            }
        } catch (Exception e) {
            LOG.error("Error occurred while finding gold type with ID {}: {}", goldId, e.getMessage(), e);
            throw new RuntimeException("Error finding gold type", e);
        }
    }

    private boolean existsById(int goldId) {
        return goldTypeRepository.existsById(goldId);
    }
}
