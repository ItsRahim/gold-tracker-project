package com.rahim.accountservice.repository;

import com.rahim.accountservice.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rahim Ahmed
 * @created 05/11/2023
 */
@Deprecated
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
}
