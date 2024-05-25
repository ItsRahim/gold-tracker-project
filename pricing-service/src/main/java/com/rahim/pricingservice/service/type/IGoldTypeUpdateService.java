package com.rahim.pricingservice.service.type;

import com.rahim.pricingservice.model.GoldType;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeUpdateService {

    GoldType updateGoldType(int goldId, Map<String, String> updatedData);
}
