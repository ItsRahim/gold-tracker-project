package com.rahim.accountservice.util;

import com.rahim.accountservice.model.EmailProperty;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.accountservice.json.AccountJson;
import com.rahim.accountservice.json.ProfileJson;
import com.rahim.common.constant.EmailTemplate;
import com.rahim.common.util.DateTimeUtil;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
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

        if (emailProperty.isIncludeUsername()) {
            accountEmailData.setUsername(rs.getString(ProfileJson.PROFILE_USERNAME));
        }

        accountEmailData.setFirstName(rs.getString(ProfileJson.PROFILE_FIRST_NAME));
        accountEmailData.setLastName(rs.getString(ProfileJson.PROFILE_LAST_NAME));
        accountEmailData.setEmail(rs.getString(AccountJson.ACCOUNT_EMAIL));
        accountEmailData.setEmailTemplate(emailProperty.getTemplateName());

        if (emailProperty.isIncludeDate()) {
            String templateName = emailProperty.getTemplateName();
            if (templateName.equals(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)) {
                LocalDate deleteDate = rs.getDate(AccountJson.ACCOUNT_DELETE_DATE).toLocalDate();
                String date = DateTimeUtil.formatDate(deleteDate);
                accountEmailData.setDeleteDate(date);
            } else if (templateName.equals(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)) {
                Instant updateAt = rs.getTimestamp(AccountJson.ACCOUNT_UPDATED_AT).toInstant();
                String date = DateTimeUtil.formatInstantDate(updateAt);
                accountEmailData.setUpdatedAt(date);
                accountEmailData.setEmail(emailProperty.getOldEmail());
            }
        }

        return accountEmailData;
    }
}
