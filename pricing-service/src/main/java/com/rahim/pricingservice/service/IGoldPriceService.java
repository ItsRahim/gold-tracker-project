package com.rahim.pricingservice.service;

import com.rahim.pricingservice.model.GoldPrice;

import java.util.Optional;

public interface IGoldPriceService {
    void updateGoldTickerPrice();
    void updateGoldPrices();
    Optional<GoldPrice> getGoldPrice(int goldId);

}
