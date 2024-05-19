package com.rahim.investmentservice.repository;

import com.rahim.investmentservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Repository
public interface TransactionRepository extends JpaRepository<Integer, Transaction> {
}
