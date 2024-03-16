package com.rahim.pricingservice.service.price;

import com.rahim.pricingservice.model.GoldType;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceCreationService {

    /**
     * Processes a new gold type and inserts its price into the repository.
     *
     * @param goldType The new gold type.
     * @throws RuntimeException If an error occurs during processing.
     */
    void processNewGoldType(GoldType goldType);
}
