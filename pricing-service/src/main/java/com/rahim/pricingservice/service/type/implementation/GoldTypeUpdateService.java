package com.rahim.pricingservice.service.type.implementation;

import com.rahim.pricingservice.entity.GoldType;
import com.rahim.pricingservice.enums.GoldPurity;
import com.rahim.pricingservice.request.GoldTypeUpdateRequest;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeUpdateService;
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
public class GoldTypeUpdateService implements IGoldTypeUpdateService {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeUpdateService.class);
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoldType updateGoldType(int goldId, GoldTypeUpdateRequest updateRequest) {
        GoldType existingGoldType = goldTypeRepositoryHandler.findById(goldId);

        if (updateRequest.getName() != null) {
            existingGoldType.setName(updateRequest.getName());
        }

        if (updateRequest.getNetWeight() != null) {
            existingGoldType.setNetWeight(updateRequest.getNetWeight());
        }

        if (updateRequest.getCarat() != null) {
            if (!caratExists(updateRequest.getCarat())) {
                throw new IllegalArgumentException("Invalid carat label: " + updateRequest.getCarat());
            }
            existingGoldType.setCarat(updateRequest.getCarat());
        }

        if (updateRequest.getDescription() != null) {
            existingGoldType.setDescription(updateRequest.getDescription());
        }

        goldTypeRepositoryHandler.updateGoldType(existingGoldType);
        LOG.debug("Successfully updated gold type with ID {}", goldId);

        return existingGoldType;
    }

    private boolean caratExists(String carat) {
        return GoldPurity.existsByCarat(carat);
    }
}
