package com.rahim.investmentservice.repository;

import com.rahim.investmentservice.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Repository
public interface HoldingRepository extends JpaRepository<Investment, Integer> {
}
