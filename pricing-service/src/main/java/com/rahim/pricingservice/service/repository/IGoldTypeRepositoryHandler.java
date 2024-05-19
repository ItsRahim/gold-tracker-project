package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.model.GoldType;

import java.util.List;
import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeRepositoryHandler {

    List<Integer> allGoldTypeIds();

    Optional<GoldType> findById(Integer goldId);

    List<GoldType> getAllGoldTypes();

    void addNewGoldType(GoldType goldType);

    void updateGoldType(GoldType goldType);

    boolean existsByName(String name);

    boolean existsById(int goldId);

    void deleteById(int goldId);

    String getGoldTypeNameById(int goldTypeId);
}
