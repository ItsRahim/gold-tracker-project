package com.rahim.pricingservice.service.type.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import com.rahim.pricingservice.service.type.IGoldTypeUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

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
    public void updateGoldType(int goldId, Map<String, String> updatedData) {
        Optional<GoldType> optionalGoldType = goldTypeRepositoryHandler.findById(goldId);

        if (optionalGoldType.isPresent()) {
            GoldType existingGoldType = optionalGoldType.get();
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
            goldTypeRepositoryHandler.updateGoldType(existingGoldType);
            LOG.debug("Successfully updated gold type with ID {}", goldId);
        } else {
            LOG.warn("GoldType with ID {} not found", goldId);
        }
    }
}
