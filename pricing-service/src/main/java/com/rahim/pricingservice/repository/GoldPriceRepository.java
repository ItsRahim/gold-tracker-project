package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Repository
public interface GoldPriceRepository extends JpaRepository<GoldPrice, Integer> {
    List<GoldPrice> findByGoldTypeId(Integer goldTypeId);

    @Query(value = "SELECT price_id FROM rgts.gold_prices WHERE gold_type_id = :goldTypeId", nativeQuery = true)
    Integer getPriceIdByTypeId(@Param("gold_type_id") int goldTypeId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (:goldTypeId, :currentPrice)", nativeQuery = true)
    void insertGoldPrice(@Param("goldTypeId") int goldTypeId, @Param("currentPrice") BigDecimal currentPrice);
}
