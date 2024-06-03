package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.entity.GoldPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Repository
public interface GoldPriceHistoryRepository extends JpaRepository<GoldPriceHistory, Integer> {
}
