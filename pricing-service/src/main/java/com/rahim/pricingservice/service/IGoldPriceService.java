package com.rahim.pricingservice.service;

import com.rahim.pricingservice.dto.GoldPriceDTO;

import java.util.List;
import java.util.Optional;

public interface IGoldPriceService {
    void updateGoldTickerPrice();
    Optional<GoldPriceDTO> getGoldPrice(int goldId);
    List<GoldPriceDTO> getAllGoldPrices();
    void processNewGoldType(String goldTypeId);
    void deleteGoldPrice(String goldTypeId);
}
