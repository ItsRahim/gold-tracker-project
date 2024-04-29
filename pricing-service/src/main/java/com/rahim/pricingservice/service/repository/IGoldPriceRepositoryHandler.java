package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.model.GoldPrice;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceRepositoryHandler {

    void saveGoldPrice(GoldPrice goldPrice);

    Optional<GoldPrice> findById(int goldId);

    List<GoldPrice> findByTypeId (int goldTypeId);

    Optional<GoldPriceDTO> getGoldPrice(int goldId);

    List<GoldPriceDTO> getAllGoldPrices();

    void deleteGoldPrice(int goldTypeId);
}
