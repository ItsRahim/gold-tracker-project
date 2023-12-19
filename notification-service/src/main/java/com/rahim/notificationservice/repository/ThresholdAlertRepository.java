package com.rahim.notificationservice.repository;

import com.rahim.notificationservice.model.ThresholdAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThresholdAlertRepository extends JpaRepository<ThresholdAlert, Integer> {
}
