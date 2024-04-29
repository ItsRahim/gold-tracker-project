package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Repository
public interface GoldTypeRepository extends JpaRepository<GoldType, Integer> {

    boolean existsByName(String name);
}
