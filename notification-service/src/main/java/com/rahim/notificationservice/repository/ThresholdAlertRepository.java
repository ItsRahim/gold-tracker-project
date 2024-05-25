package com.rahim.notificationservice.repository;

import com.rahim.notificationservice.dao.NotificationDataAccess;
import com.rahim.notificationservice.model.NotificationResult;
import com.rahim.notificationservice.model.ThresholdAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ThresholdAlertRepository extends JpaRepository<ThresholdAlert, Integer> {

    List<ThresholdAlert> findByIsActiveTrue();

    ThresholdAlert findThresholdAlertByAccountId(int accountId);

@Query(value =
        "SELECT " +
                NotificationDataAccess.COL_ALERT_ID + " AS alertID, " +
                NotificationDataAccess.PROFILE_COL_FIRST_NAME + " AS firstName, " +
                NotificationDataAccess.PROFILE_COL_LAST_NAME + " AS lastName, " +
                NotificationDataAccess.ACCOUNT_COL_EMAIL + " AS email, " +
                NotificationDataAccess.COL_THRESHOLD_PRICE + " AS thresholdPrice" +
                " FROM " +
                NotificationDataAccess.PROFILE_TABLE_NAME + " up" +
                " JOIN " + NotificationDataAccess.TABLE_NAME + " ta ON up." + NotificationDataAccess.COL_ACCOUNT_ID + " = ta." + NotificationDataAccess.COL_ACCOUNT_ID +
                " JOIN " + NotificationDataAccess.ACCOUNT_TABLE_NAME + " u ON up." + NotificationDataAccess.ACCOUNT_COL_ACCOUNT_ID + " = u." + NotificationDataAccess.ACCOUNT_COL_ACCOUNT_ID +
                " WHERE " +
                "ta." + NotificationDataAccess.COL_THRESHOLD_PRICE + " = :thresholdPrice" +
                " AND ta." + NotificationDataAccess.COL_IS_ACTIVE + " = 'true'",
        nativeQuery = true)
List<NotificationResult> generateEmailTokens(@Param("thresholdPrice") BigDecimal thresholdPrice);

}
