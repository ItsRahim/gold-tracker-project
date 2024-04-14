package com.rahim.pricingservice.service.repository.implementation;

import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldTypeRepository;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldTypeRepositoryHandler implements IGoldTypeRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeRepositoryHandler.class);

    private final GoldTypeRepository goldTypeRepository;

    @Override
    public List<Integer> allGoldTypeIds() {
        try {
            List<Integer> ids = goldTypeRepository.findAll()
                    .stream()
                    .map(GoldType::getId)
                    .collect(Collectors.toList());

            LOG.debug("Retrieved all GoldType IDs: {}", ids);

            return ids;
        } catch (Exception e) {
            LOG.error("Error getting all GoldType IDs", e);
            throw new RuntimeException("Error getting all GoldType IDs", e);
        }
    }

    @Override
    public Optional<GoldType> findById(Integer goldTypeId) {
        try {
            Objects.requireNonNull(goldTypeId, "Gold Type ID must not be null");

            Optional<GoldType> typeOptional = goldTypeRepository.findById(goldTypeId);

            if (typeOptional.isPresent()) {
                LOG.debug("Found Gold Type with ID: {}", goldTypeId);
            } else {
                LOG.debug("Gold Type not found for ID: {}", goldTypeId);
            }

            return typeOptional;
        } catch (DataAccessException e) {
            String errorMessage = "An error occurred while retrieving Gold Type with ID: " + goldTypeId;
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
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
    public void saveGoldType(GoldType goldType) {
        if (!ObjectUtils.allNotNull(goldType, goldType.getName(), goldType.getNetWeight(), goldType.getCarat(), goldType.getDescription())) {
            LOG.error("GoldType object is null or contains null properties. Unable to save.");
            throw new IllegalArgumentException("GoldType object is null or contains null properties. Unable to save.");
        }

        try {
            goldTypeRepository.save(goldType);
        } catch (DataException e) {
            LOG.error("Error saving gold type to the database", e);
            throw new DataIntegrityViolationException("Error saving gold type to the database", e);
        }
    }

    @Override
    public boolean existsByName(String name) {
        return goldTypeRepository.existsByName(name);
    }

    @Override
    public boolean existsById(int goldId) {
        return goldTypeRepository.existsById(goldId);
    }

    @Override
    public void deleteById(int goldId) {
        goldTypeRepository.deleteById(goldId);
    }

}