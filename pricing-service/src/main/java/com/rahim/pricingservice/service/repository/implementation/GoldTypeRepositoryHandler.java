package com.rahim.pricingservice.service.repository.implementation;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@Transactional
@RequiredArgsConstructor
public class GoldTypeRepositoryHandler implements IGoldTypeRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GoldTypeRepositoryHandler.class);
    private final GoldTypeRepository goldTypeRepository;

    @Override
    public List<Integer> allGoldTypeIds() {
        try {
            return goldTypeRepository
                    .findAll()
                    .stream()
                    .map(GoldType::getId)
                    .toList();
        } catch (Exception e) {
            LOG.error("Error getting all GoldType IDs", e);
            throw new RuntimeException("Error getting all GoldType IDs", e);
        }
    }

    @Override
    public Optional<GoldType> findById(Integer goldTypeId) {
        try {
            Objects.requireNonNull(goldTypeId, "Gold Type ID must not be null");
            return goldTypeRepository.findById(goldTypeId);
        } catch (DataAccessException e) {
            LOG.error("An error occurred while retrieving Gold Type with ID: {}", goldTypeId, e);
        }
        return Optional.empty();
    }

    @Override
    public List<GoldType> getAllGoldTypes() {
        return goldTypeRepository.findAll();
    }

    @Override
    public void addNewGoldType(GoldType goldType) {
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
    public void updateGoldType(GoldType goldType) {
        if (goldType == null || goldType.getId() == null) {
            LOG.error("Invalid gold type or gold type ID is null. Unable to save.");
            throw new IllegalArgumentException("Invalid gold type or gold type ID is null. Unable to save.");
        }

        try {
            goldTypeRepository.save(goldType);
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error updating profile to the database: {}", e.getMessage());
            throw new RuntimeException("Error saving profile to database", e);
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
