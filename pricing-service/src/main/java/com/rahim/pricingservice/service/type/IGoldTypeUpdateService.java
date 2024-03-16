package com.rahim.pricingservice.service.type;

import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeUpdateService {

    void updateGoldType(int goldId, Map<String, String> updatedData) throws Exception;
}
