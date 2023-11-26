package com.rahim.emailservice.repository;

import com.rahim.emailservice.model.EmailHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailHistoryRepository extends JpaRepository<EmailHistory, Integer> {
}
