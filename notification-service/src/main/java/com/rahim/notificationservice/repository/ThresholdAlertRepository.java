package com.rahim.notificationservice.repository;

import com.rahim.notificationservice.entity.ThresholdAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThresholdAlertRepository extends JpaRepository<ThresholdAlert, Integer> {

    List<ThresholdAlert> findByIsActiveTrue();

    ThresholdAlert findThresholdAlertByAccountId(int accountId);

}
