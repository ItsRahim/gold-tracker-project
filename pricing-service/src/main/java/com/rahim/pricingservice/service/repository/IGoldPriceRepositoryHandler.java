package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.dto.GoldPriceDTO;
import com.rahim.pricingservice.entity.GoldPrice;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceRepositoryHandler {

    void saveGoldPrice(GoldPrice goldPrice);

    GoldPrice findById(int goldId);

    List<GoldPrice> findByTypeId (int goldTypeId);

    GoldPriceDTO getGoldPrice(int goldId);

    List<GoldPriceDTO> getAllGoldPrices();

    void deleteGoldPrice(int goldTypeId);
}
