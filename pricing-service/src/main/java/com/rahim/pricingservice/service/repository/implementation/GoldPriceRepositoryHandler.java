package com.rahim.pricingservice.service.repository.implementation;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.price.implementation.GoldPriceCreationService;
import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
@Service
@RequiredArgsConstructor
public class GoldPriceRepositoryHandler implements IGoldPriceRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceCreationService.class);

    private final GoldPriceRepository goldPriceRepository;
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;

    @Override
    public void saveGoldPrice(GoldPrice goldPrice) {
        if (!ObjectUtils.allNotNull(goldPrice, goldPrice.getCurrentPrice(), goldPrice.getGoldType(), goldPrice.getUpdatedAt())) {
            LOG.error("GoldPrice object is null or contains null properties. Unable to save.");
            throw new IllegalArgumentException("GoldPrice object is null or contains null properties. Unable to save.");
        }

        try {
            goldPriceRepository.save(goldPrice);
        } catch (DataException e) {
            LOG.error("Error saving gold price to the database", e);
            throw new DataIntegrityViolationException("Error saving gold price to the database", e);
        }
    }

    @Override
    public Optional<GoldPrice> findById(int goldId) {
        try {
            Optional<GoldPrice> priceOptional = goldPriceRepository.findById(goldId);

            if (priceOptional.isPresent()) {
                LOG.debug("Found GoldPrice with ID: {}", goldId);
            } else {
                LOG.debug("GoldPrice not found for ID: {}", goldId);
            }

            return priceOptional;
        } catch (DataAccessException e) {
            String errorMessage = "An error occurred while retrieving GoldPrice with ID: " + goldId;
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public List<GoldPrice> findByTypeId(int goldTypeId) {
        return goldPriceRepository.findByGoldTypeId(goldTypeId);
    }


    @Override
    public Optional<GoldPriceDTO> getGoldPrice(int goldId) {
        try {
            Optional<GoldPrice> goldPriceOptional = findById(goldId);
            return goldPriceOptional.map(this::mapToGoldPriceDTO);
        } catch (DataAccessException e) {
            String errorMessage = "An error occurred while retrieving GoldPrice with ID: " + goldId;
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public List<GoldPriceDTO> getAllGoldPrices() {
        try {
            List<GoldPrice> goldPrices = goldPriceRepository.findAll();
            return goldPrices.stream()
                    .map(this::mapToGoldPriceDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            String errorMessage = "Error getting all gold prices: " + e.getMessage();
            LOG.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public void deleteGoldPrice(int goldTypeId) {
        try {
            Integer priceId = goldPriceRepository.getPriceIdByTypeId(goldTypeId);
            if (priceId != null) {
                goldPriceRepository.deleteById(priceId);
                LOG.info("Gold type with ID {} and associated price deleted successfully.", goldTypeId);
            } else {
                LOG.warn("Gold type with ID {} not found. Unable to delete associated price.", goldTypeId);
            }
        } catch (DataAccessException  e) {
            LOG.error("Error deleting gold type with ID {}: {}", goldTypeId, e.getMessage());
            throw new RuntimeException("Error deleting gold type", e);
        }
    }

    /**
     * Maps a GoldPrice entity to a GoldPriceDTO.
     *
     * @param goldPrice The GoldPrice entity to map.
     * @return The corresponding GoldPriceDTO object.
     * @throws NoSuchElementException if the corresponding GoldType is not found.
     */
    private GoldPriceDTO mapToGoldPriceDTO(GoldPrice goldPrice) {
        GoldType goldType = goldTypeRepositoryHandler.findById(goldPrice.getGoldType().getId())
                .orElseThrow(() -> new NoSuchElementException("Gold Type not found"));
        return new GoldPriceDTO(
                goldPrice.getId(),
                goldType.getName(),
                goldPrice.getCurrentPrice(),
                goldPrice.getUpdatedAt()
        );
    }

}
