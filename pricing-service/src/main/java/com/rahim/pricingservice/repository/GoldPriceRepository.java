package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.dao.GoldPriceDataAccess;
import com.rahim.pricingservice.dao.GoldTypeDataAccess;
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

    @Query(value = "SELECT " + GoldPriceDataAccess.COL_GOLD_PRICE_ID +
            " FROM " + GoldPriceDataAccess.TABLE_NAME +
            " WHERE " + GoldPriceDataAccess.COL_GOLD_TYPE_ID +
            " = :goldTypeId", nativeQuery = true)
    Integer getPriceIdByTypeId(@Param("goldTypeId") int goldTypeId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO " + GoldPriceDataAccess.TABLE_NAME +
            "( " + GoldTypeDataAccess.COL_GOLD_TYPE_ID + ", " + GoldPriceDataAccess.COL_CURRENT_PRICE + ")"
            + " VALUES (:goldTypeId, :currentPrice)", nativeQuery = true)
    void insertGoldPrice(@Param("goldTypeId") int goldTypeId, @Param("currentPrice") BigDecimal currentPrice);
}
