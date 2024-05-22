package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.dao.GoldPriceDataAccess;
import com.rahim.pricingservice.model.GoldPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Repository
public interface GoldPriceRepository extends JpaRepository<GoldPrice, Integer> {

    List<GoldPrice> findByGoldTypeId(Integer goldTypeId);

    @Query(value = "SELECT "
            + GoldPriceDataAccess.COL_GOLD_PRICE_ID
            + " FROM "
            + GoldPriceDataAccess.TABLE_NAME
            + " WHERE "
            + GoldPriceDataAccess.COL_GOLD_TYPE_ID +
            " = :goldTypeId", nativeQuery = true)
    Integer getPriceIdByTypeId(@Param("goldTypeId") int goldTypeId);
}
