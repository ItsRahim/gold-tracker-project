package com.rahim.pricingservice.repository;

import com.rahim.pricingservice.dao.GoldTypeDataAccess;
import com.rahim.pricingservice.entity.GoldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 29/11/2023
 */
@Repository
public interface GoldTypeRepository extends JpaRepository<GoldType, Integer> {

    boolean existsByName(String name);

    @Query(value = "SELECT "
            + GoldTypeDataAccess.COL_TYPE_NAME
            + " FROM "
            + GoldTypeDataAccess.TABLE_NAME
            + " WHERE "
            + GoldTypeDataAccess.COL_GOLD_TYPE_ID
            + " = :goldTypeId", nativeQuery = true)
    String getGoldTypeNameById(int goldTypeId);

    @Query(value = "SELECT "
            + GoldTypeDataAccess.COL_TYPE_NAME
            + ", "
            + GoldTypeDataAccess.COL_GOLD_TYPE_ID
            + " FROM "
            + GoldTypeDataAccess.TABLE_NAME, nativeQuery = true)
    List<Object[]> getGoldTypeNameAndId();
}
