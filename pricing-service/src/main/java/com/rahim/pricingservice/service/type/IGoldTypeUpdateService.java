package com.rahim.pricingservice.service.type;

import com.rahim.pricingservice.entity.GoldType;
import com.rahim.pricingservice.request.GoldTypeUpdateRequest;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeUpdateService {

    GoldType updateGoldType(int goldId, GoldTypeUpdateRequest updatedData);
}
