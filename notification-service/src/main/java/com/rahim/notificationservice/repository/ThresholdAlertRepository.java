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
    @Query(value = "SELECT up.first_name, up.last_name, u.email, ta.threshold_price, ta.is_active, ta.alert_id " +
            "FROM rgts.user_profiles up " +
            "JOIN rgts.threshold_alerts ta ON up.account_id = ta.account_id " +
            "JOIN rgts.user_accounts u ON up.account_id = u.account_id " +
            "WHERE ta.threshold_price = :thresholdPrice", nativeQuery = true)
    List<Map<String, Object>> getEmailTokens(@Param("thresholdPrice") BigDecimal thresholdPrice);

}
