package com.rahim.investmentservice.repository;

import com.rahim.investmentservice.dao.InvestmentsDataAccess;
import com.rahim.investmentservice.entity.Investment;
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
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {

    @Query(value = "SELECT COUNT(*) > 0 FROM "
            + InvestmentsDataAccess.TABLE_NAME
            + " WHERE "
            + InvestmentsDataAccess.COL_INVESTMENT_ID + " = :investmentId"
            + " AND "
            + InvestmentsDataAccess.COL_ACCOUNT_ID + " = :accountId", nativeQuery = true)
    boolean existsInvestmentByAccountId(@Param("investmentId") int investmentId, @Param("accountId") int accountId);

    Optional<Investment> getInvestmentById(int investmentId);

}
