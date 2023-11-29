package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldPriceHistoryRepository extends JpaRepository<GoldPriceHistory, Integer> {
}
