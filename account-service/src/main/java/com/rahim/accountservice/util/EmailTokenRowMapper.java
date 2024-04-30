package com.rahim.accountservice.util;

import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import com.rahim.accountservice.request.AccountRequest;
import com.rahim.accountservice.request.ProfileRequest;
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
public class EmailTokenRowMapper implements RowMapper<EmailToken> {

    private EmailProperty emailProperty;

    @Override
    public EmailToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmailToken emailToken = new EmailToken();

        if (emailProperty.isIncludeUsername()) {
            emailToken.setUsername(rs.getString(ProfileRequest.PROFILE_USERNAME));
        }

        emailToken.setFirstName(rs.getString(ProfileRequest.PROFILE_FIRST_NAME));
        emailToken.setLastName(rs.getString(ProfileRequest.PROFILE_LAST_NAME));
        emailToken.setEmail(rs.getString(AccountRequest.ACCOUNT_EMAIL));
        emailToken.setEmailTemplate(emailProperty.getTemplateName());

        if (emailProperty.isIncludeDate()) {
            String templateName = emailProperty.getTemplateName();
            if (templateName.equals(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)) {
                LocalDate deleteDate = rs.getDate(AccountRequest.ACCOUNT_DELETE_DATE).toLocalDate();
                String date = DateFormatter.getInstance().formatDate(deleteDate);
                emailToken.setDeleteDate(date);
            } else if (templateName.equals(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)) {
                Instant updateAt = rs.getTimestamp(AccountRequest.ACCOUNT_UPDATED_AT).toInstant();
                String date = DateFormatter.getInstance().formatInstantDate(updateAt);
                emailToken.setUpdatedAt(date);
                emailToken.setEmail(emailProperty.getOldEmail());
            }
        }

        return emailToken;
    }
}
