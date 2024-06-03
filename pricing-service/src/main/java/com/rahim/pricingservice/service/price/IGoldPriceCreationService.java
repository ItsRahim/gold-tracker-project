package com.rahim.pricingservice.service.price;

import com.rahim.pricingservice.entity.GoldType;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldPriceCreationService {

    void processNewGoldType(GoldType goldType);
}
