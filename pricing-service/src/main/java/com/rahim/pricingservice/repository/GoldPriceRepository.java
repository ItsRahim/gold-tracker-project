package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldPriceRepository extends JpaRepository<GoldPrice, Integer> {
}
