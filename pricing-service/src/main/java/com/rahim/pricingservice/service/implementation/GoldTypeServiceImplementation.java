package com.rahim.pricingservice.service.implementation;

import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldTypeRepository;
import com.rahim.pricingservice.service.IGoldTypeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoldTypeServiceImplementation implements IGoldTypeService {
    private final Logger LOG = LoggerFactory.getLogger(GoldPriceServiceImplementation.class);
    private final GoldTypeRepository goldTypeRepository;

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
}
