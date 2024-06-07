package com.rahim.notificationservice.util;

import com.rahim.notificationservice.dao.NotificationDataAccess;
import com.rahim.notificationservice.model.NotificationResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Rahim Ahmed
 * @created 07/06/2024
 */
public class NotificationResultRowMapper implements RowMapper<NotificationResult> {

    @Override
    public NotificationResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        Integer alertId = rs.getInt(NotificationDataAccess.COL_ALERT_ID);
        String firstName = rs.getString(NotificationDataAccess.PROFILE_COL_FIRST_NAME);
        String lastName = rs.getString(NotificationDataAccess.PROFILE_COL_LAST_NAME);
        String email = rs.getString(NotificationDataAccess.ACCOUNT_COL_EMAIL);
        double price = rs.getDouble(NotificationDataAccess.COL_THRESHOLD_PRICE);
        return new NotificationResult(alertId, firstName, lastName, email, price);
    }
}
