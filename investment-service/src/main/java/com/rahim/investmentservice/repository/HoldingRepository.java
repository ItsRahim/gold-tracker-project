package com.rahim.investmentservice.repository;

import com.rahim.investmentservice.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Integer> {
}
