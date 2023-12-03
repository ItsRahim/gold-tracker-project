package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.model.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoldPriceRepository extends JpaRepository<GoldPrice, Integer> {
    List<GoldPrice> findByGoldTypeId(Integer goldTypeId);

    @Modifying
    @Query(value = "INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (:gold_type_id, 0)", nativeQuery = true)
    void insertGoldType(@Param("gold_type_id") int gold_type_id);

    @Query(value = "SELECT price_id FROM rgts.gold_prices WHERE gold_type_id = :gold_type_id", nativeQuery = true)
    Integer getPriceIdByTypeId(@Param("gold_type_id") int gold_type_id);
}
