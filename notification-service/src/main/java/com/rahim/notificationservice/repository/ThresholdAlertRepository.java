package com.rahim.notificationservice.repository;

import com.rahim.notificationservice.model.ThresholdAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface ThresholdAlertRepository extends JpaRepository<ThresholdAlert, Integer> {
    @Query(value = "SELECT up.first_name AS firstName, up.last_name AS lastName, u.email, ta.threshold_price AS thresholdPrice " +
            "FROM rgts.user_profiles up " +
            "JOIN rgts.threshold_alerts ta ON up.user_id = ta.user_id " +
            "JOIN rgts.users u ON up.user_id = u.user_id " +
            "WHERE ta.threshold_price = :threshold_price", nativeQuery = true)
    List<Map<String, Object>> getEmailTokens(@Param("threshold_price") BigDecimal threshold_price);
}
