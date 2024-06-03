package com.rahim.pricingservice.service.repository;

import com.rahim.pricingservice.entity.GoldType;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 16/03/2024
 */
public interface IGoldTypeRepositoryHandler {

    List<Integer> allGoldTypeIds();

    GoldType findById(Integer goldId);

    List<GoldType> getAllGoldTypes();

    void addNewGoldType(GoldType goldType);

    void updateGoldType(GoldType goldType);

    boolean existsByName(String name);

    void deleteById(int goldId);

    String getGoldTypeNameById(int goldTypeId);

    List<Object[]> getGoldTypeNameAndId();
}
