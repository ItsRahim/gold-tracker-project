package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldTypeRepository extends JpaRepository<GoldType, Integer> {
}
