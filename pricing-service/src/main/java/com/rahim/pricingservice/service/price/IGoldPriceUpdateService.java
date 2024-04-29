package com.rahim.pricingservice.service.price;

import com.rahim.pricingservice.model.GoldData;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceUpdateService {

    void updateGoldTickerPrice(GoldData processedData);
}
