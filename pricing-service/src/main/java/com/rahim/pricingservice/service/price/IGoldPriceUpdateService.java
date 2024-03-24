package com.rahim.pricingservice.service.price;

import com.rahim.pricingservice.model.GoldData;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceUpdateService {

    /**
     * Service method for updating the gold ticker price.
     * Retrieves data from an external API, processes it, and updates the gold ticker price in the repository.
     */
    void updateGoldTickerPrice(GoldData processedData);
}
