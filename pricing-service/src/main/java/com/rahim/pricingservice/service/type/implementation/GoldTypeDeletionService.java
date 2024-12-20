package com.rahim.pricingservice.service.type.implementation;

import com.rahim.common.constant.HazelcastConstant;
import com.rahim.common.service.hazelcast.CacheManager;
import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeDeletionService;
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
public class GoldTypeDeletionService implements IGoldTypeDeletionService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeDeletionService.class);
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;
    private final IGoldPriceRepositoryHandler goldPriceRepositoryHandler;
    private final CacheManager hazelcastCacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoldType(int goldId) {
        try {
            goldPriceRepositoryHandler.deleteGoldPrice(goldId);
            goldTypeRepositoryHandler.deleteById(goldId);
            removeFromHazelcast(goldId);
            LOG.debug("Gold type with ID {} deleted successfully.", goldId);
        } catch (Exception e) {
            LOG.error("An error has occurred whilst attempting to delete gold type with ID: {}", goldId);
            throw new RuntimeException("Error deleting gold type");
        }
    }

    private void removeFromHazelcast(int goldId) {
        String goldTypeName = goldTypeRepositoryHandler.getGoldTypeNameById(goldId);
        hazelcastCacheManager.removeFromMap(HazelcastConstant.GOLD_TYPE_MAP, goldTypeName);
    }
}
