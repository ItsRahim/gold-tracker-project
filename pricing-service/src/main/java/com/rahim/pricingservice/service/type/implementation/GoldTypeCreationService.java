package com.rahim.pricingservice.service.type.implementation;

import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.price.IGoldPriceCreationService;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeCreationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
    public void addGoldType(GoldType goldType) {
        try {
            if (!ObjectUtils.anyNull(goldType)) {
                goldTypeRepositoryHandler.addNewGoldType(goldType);
                LOG.debug("Successfully added new gold type: {}", goldType.getName());
                goldPriceCreationService.processNewGoldType(goldType);
                addToHazelcast(goldType.getId(), goldType.getName());
            } else {
                LOG.warn("Given gold types has one or more null values. Not adding to database");
            }
        } catch (Exception e) {
            LOG.error("Unexpected error adding new gold type: {}", e.getMessage());
            throw new RuntimeException("Unexpected error adding new gold type", e);
        }
    }

    private void addToHazelcast(Integer goldTypeId, String goldTypeName) {
        hazelcastCacheManager.addToMap(HazelcastConstant.GOLD_TYPE_MAP, goldTypeName, goldTypeId);
        LOG.debug("Added {} to Hazelcast map", goldTypeName);
    }
}
