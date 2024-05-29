package com.rahim.pricingservice.service.repository.implementation;

import com.rahim.common.exception.DatabaseException;
import com.rahim.common.exception.EntityNotFoundException;
import com.rahim.common.exception.ValidationException;
import com.rahim.common.util.DateTimeUtil;
import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.model.GoldPrice;
import com.rahim.pricingservice.model.GoldType;
import com.rahim.pricingservice.repository.GoldPriceRepository;
import com.rahim.pricingservice.service.repository.IGoldPriceRepositoryHandler;
import com.rahim.pricingservice.service.repository.IGoldTypeRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
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
public class GoldPriceRepositoryHandler implements IGoldPriceRepositoryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GoldPriceRepositoryHandler.class);
    private final GoldPriceRepository goldPriceRepository;
    private final IGoldTypeRepositoryHandler goldTypeRepositoryHandler;

    @Override
    public void saveGoldPrice(GoldPrice goldPrice) {
        if (!ObjectUtils.allNotNull(goldPrice, goldPrice.getCurrentPrice(), goldPrice.getGoldType())) {
            LOG.error("GoldPrice object is null or contains null properties. Unable to save.");
            throw new ValidationException("GoldPrice object is null or contains null properties. Unable to save.");
        }

        try {
            goldPrice.setUpdatedAt(DateTimeUtil.generateInstant());
            goldPriceRepository.save(goldPrice);
        } catch (DataAccessException e) {
            LOG.error("Error saving gold price to the database", e);
            throw new DatabaseException("Error saving gold price to the database");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GoldPrice findById(int goldId) {
        return goldPriceRepository.findById(goldId)
                .orElseThrow(() -> new EntityNotFoundException("Gold price not found with ID " + goldId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GoldPrice> findByTypeId(int goldTypeId) {
        return goldPriceRepository.findByGoldTypeId(goldTypeId);
    }

    @Override
    @Transactional(readOnly = true)
    public GoldPriceDTO getGoldPrice(int goldId) {
        GoldPrice goldPrice = this.findById(goldId);
        GoldType goldType = goldTypeRepositoryHandler.findById(goldPrice.getGoldType().getId());

        return new GoldPriceDTO(goldPrice.getId(), goldType.getName(), goldPrice.getCurrentPrice(), goldPrice.getUpdatedAt());
    }


    @Override
    @Transactional(readOnly = true)
    public List<GoldPriceDTO> getAllGoldPrices() {
        try {
            List<Integer> goldPriceIds = goldPriceRepository
                    .findAll()
                    .stream()
                    .map(GoldPrice::getId)
                    .toList();

            return goldPriceIds.stream()
                    .map(this::getGoldPrice)
                    .toList();

        } catch (DataAccessException e) {
            String errorMessage = "Error getting all gold prices: " + e.getMessage();
            LOG.error(errorMessage, e);
            throw new DatabaseException(errorMessage);
        }
    }

    @Override
    public void deleteGoldPrice(int goldTypeId) {
        try {
            Integer priceId = goldPriceRepository.getPriceIdByTypeId(goldTypeId);
            if (priceId == null) {
                LOG.warn("Gold type with ID {} not found. Unable to delete associated price.", goldTypeId);
                throw new EntityNotFoundException("Gold type not found for ID: " + goldTypeId);
            }

            goldPriceRepository.deleteById(priceId);
            LOG.debug("Gold type with ID {} and associated price deleted successfully.", goldTypeId);
        } catch (DataAccessException  e) {
            LOG.error("Error deleting gold type with ID {}: {}", goldTypeId, e.getMessage());
            throw new DatabaseException("Error deleting gold type");
        }
    }

}
