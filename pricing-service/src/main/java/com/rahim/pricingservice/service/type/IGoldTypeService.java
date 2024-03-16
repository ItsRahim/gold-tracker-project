package com.rahim.pricingservice.service.type;

import com.rahim.pricingservice.model.GoldType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IGoldTypeService {
    List<Integer> getAllIds();
    void addGoldType(GoldType goldType) throws Exception;
    void updateGoldType(int goldId, Map<String, String> updatedData) throws Exception;
    void deleteGoldType(int goldId);
    List<GoldType> getAllGoldTypes();
    Optional<GoldType> findById(int goldId);
}
