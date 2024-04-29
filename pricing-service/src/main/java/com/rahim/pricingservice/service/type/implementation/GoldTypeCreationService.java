package com.rahim.pricingservice.service.type.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.price.IGoldPriceCreationService;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeCreationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    @Override
    public void addGoldType(GoldType goldType) {
        try {
            if (!ObjectUtils.anyNull(goldType)) {
                goldTypeRepositoryHandler.addNewGoldType(goldType);
                LOG.info("Successfully added new gold type: {}", goldType.getName());
                goldPriceCreationService.processNewGoldType(goldType);
            } else {
                LOG.warn("Given gold types has one or more null values. Not adding to database");
            }
        } catch (Exception e) {
            LOG.error("Unexpected error adding new gold type: {}", e.getMessage());
            throw new RuntimeException("Unexpected error adding new gold type", e);
        }
    }
}
