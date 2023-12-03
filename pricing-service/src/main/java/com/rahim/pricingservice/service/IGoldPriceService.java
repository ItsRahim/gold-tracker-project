package com.rahim.pricingservice.service;

import com.rahim.pricingservice.model.GoldPrice;

import java.util.Optional;

public interface IGoldPriceService {
    void setKafkaData(String data);
    void updateGoldTickerPrice();
    Optional<GoldPrice> getGoldPrice(int goldId);

}
