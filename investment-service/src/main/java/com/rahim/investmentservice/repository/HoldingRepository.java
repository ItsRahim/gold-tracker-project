package com.rahim.investmentservice.repository;

import com.rahim.investmentservice.dao.HoldingDataAccess;
import com.rahim.investmentservice.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Integer> {

    boolean existsHoldingById(int holdingId);
    Holding getHoldingById(int holdingId);

    @Query(value = "SELECT * FROM " + HoldingDataAccess.TABLE_NAME +
            " WHERE " + HoldingDataAccess.COL_HOLDING_ID + " = :holdingId" +
            " AND " + HoldingDataAccess.COL_ACCOUNT_ID + " = :accountId", nativeQuery = true)
    Optional<Holding> getHoldingByIdAndAccountId(@Param("holdingId") int holdingId, @Param("accountId") int accountId);

}
