package com.rahim.pricingservice.service.type.implementation;

import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeDeletionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    @Override
    public void deleteGoldType(int goldId) {
        try {
            if (!goldTypeRepositoryHandler.existsById(goldId)) {
                LOG.warn("Gold type with ID: {} does not exist. Unable to delete.", goldId);
            }
            goldPriceRepositoryHandler.deleteGoldPrice(goldId);
            goldTypeRepositoryHandler.deleteById(goldId);

            LOG.info("Gold type with ID {} deleted successfully.", goldId);
        } catch (Exception e) {
            LOG.error("An error has occurred whilst attempting to delete gold type with ID: {}", goldId);
            throw new RuntimeException(e);
        }
    }
}