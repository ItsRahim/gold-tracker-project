package com.rahim.userservice.util;

import com.rahim.userservice.dao.AccountDataAccess;
import com.rahim.userservice.dao.ProfileDataAccess;
import com.rahim.userservice.model.EmailProperty;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.common.util.DateTimeUtil;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * @author Rahim Ahmed
 * @created 28/04/2024
 */
@Setter
public class EmailTokenRowMapper implements RowMapper<AccountEmailData> {

    private EmailProperty emailProperty;

    @Override
    public AccountEmailData mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccountEmailData accountEmailData = new AccountEmailData();
        try {
            if (emailProperty.isIncludeUsername()) {
                accountEmailData.setUsername(rs.getString(ProfileDataAccess.COL_PROFILE_USERNAME));
            }

            accountEmailData.setFirstName(rs.getString(ProfileDataAccess.COL_PROFILE_FIRST_NAME));
            accountEmailData.setLastName(rs.getString(ProfileDataAccess.COL_PROFILE_LAST_NAME));
            accountEmailData.setEmail(rs.getString(AccountDataAccess.COL_EMAIL));
            accountEmailData.setEmailTemplate(emailProperty.getTemplateName());

            if (emailProperty.isIncludeDate()) {
                handleDateInclusion(rs, accountEmailData);
            }
        } catch (SQLException e) {
            throw new SQLException("Error mapping row to AccountEmailData", e);
        }

        return accountEmailData;
    }

    private void handleDateInclusion(ResultSet rs, AccountEmailData accountEmailData) throws SQLException {
        EmailTemplate templateName = emailProperty.getTemplateName();

        try {
            switch (templateName) {
                case ACCOUNT_DELETION:
                    setDeleteDate(rs, accountEmailData);
                    break;
                case ACCOUNT_UPDATE:
                    setUpdateDateAndOldEmail(accountEmailData);
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            throw new SQLException("Error handling date inclusion for template: " + templateName, e);
        }
    }

    private void setDeleteDate(ResultSet rs, AccountEmailData accountEmailData) throws SQLException {
        try {
            LocalDate deleteDate = rs.getDate(AccountDataAccess.COL_DELETE_DATE).toLocalDate();
            String formattedDate = DateTimeUtil.getFormattedDate(deleteDate);
            accountEmailData.setDeleteDate(formattedDate);
        } catch (SQLException e) {
            throw new SQLException("Error setting delete date", e);
        }
    }

    private void setUpdateDateAndOldEmail(AccountEmailData accountEmailData) {
        String formattedDate = DateTimeUtil.getFormattedInstant();
        accountEmailData.setUpdatedAt(formattedDate);
        accountEmailData.setEmail(emailProperty.getOldEmail());
    }
}
