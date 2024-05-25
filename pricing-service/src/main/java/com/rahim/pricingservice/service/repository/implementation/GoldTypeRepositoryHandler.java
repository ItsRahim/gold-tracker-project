package com.rahim.pricingservice.service.repository.implementation;

import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldTypeRepository;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional(readOnly = true)
    public List<Integer> allGoldTypeIds() {
        try {
            return goldTypeRepository
                    .findAll()
                    .stream()
                    .map(GoldType::getId)
                    .toList();
        } catch (DataAccessException e) {
            LOG.error("Error getting all Gold Type IDs: {}", e.getMessage());
            throw new DatabaseException("Error getting all Gold Type IDs");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GoldType findById(Integer goldTypeId) {
        return goldTypeRepository.findById(goldTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Gold type not found with ID " + goldTypeId));
    }

    @Override
    @Transactional(readOnly = true)
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
        } catch (DataAccessException e) {
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
        } catch (DataAccessException e) {
            LOG.error("Error updating profile to the database: {}", e.getMessage());
            throw new DatabaseException("Error saving profile to database");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return goldTypeRepository.existsByName(name);
    }

    @Override
    public void deleteById(int goldId) {
        goldTypeRepository.deleteById(goldId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getGoldTypeNameById(int goldTypeId) {
        return goldTypeRepository.getGoldTypeNameById(goldTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getGoldTypeNameAndId() {
        return goldTypeRepository.getGoldTypeNameAndId();
    }

}
