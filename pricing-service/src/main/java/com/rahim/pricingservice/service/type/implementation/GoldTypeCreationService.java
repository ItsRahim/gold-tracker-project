package com.rahim.pricingservice.service.type.implementation;

import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.exception.DuplicateEntityException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.common.util.InputValidator;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.price.IGoldPriceCreationService;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeCreationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldTypeCreationService implements IGoldTypeCreationService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeCreationService.class);
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;
    private final IGoldPriceCreationService goldPriceCreationService;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoldType addGoldType(GoldType goldType) {
        if (InputValidator.validateObjectFields(goldType, "name", "netWeight", "carat", "description")) {
            LOG.warn("Invalid gold type object: {}", goldType);
            throw new ValidationException("Invalid gold type object");
        }

        String name = goldType.getName();
        if (goldTypeRepositoryHandler.existsByName(name)) {
            LOG.warn("Gold Type with name: {} already exists", name);
            throw new DuplicateEntityException("Gold type already exists with name " + name);
        }

        try {
            goldTypeRepositoryHandler.addNewGoldType(goldType);
            LOG.debug("Successfully added new gold type: {}", goldType.getName());
            goldPriceCreationService.processNewGoldType(goldType);
            addToHazelcast(goldType.getId(), goldType.getName());

            return goldType;
        } catch (RuntimeException e) {
            LOG.error("Unexpected error adding new gold type: {}", e.getMessage());
            throw new RuntimeException("Unexpected error adding new gold type", e);
        }
    }

    private void addToHazelcast(Integer goldTypeId, String goldTypeName) {
        hazelcastCacheManager.addToMap(HazelcastConstant.GOLD_TYPE_MAP, goldTypeName, goldTypeId);
        LOG.debug("Added {} to Hazelcast map", goldTypeName);
    }
}
