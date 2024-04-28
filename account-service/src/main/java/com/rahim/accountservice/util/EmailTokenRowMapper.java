package com.rahim.accountservice.util;

import com.rahim.accountservice.constant.EmailTemplate;
import com.rahim.accountservice.model.EmailProperty;
import com.rahim.accountservice.model.EmailToken;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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
            emailToken.setUsername(rs.getString("username"));
        }

        emailToken.setFirstName(rs.getString("firstName"));
        emailToken.setLastName(rs.getString("lastName"));
        emailToken.setEmail(rs.getString("email"));
        emailToken.setEmailTemplate(emailProperty.getTemplateName());

        if (emailProperty.isIncludeDate()) {
            String templateName = emailProperty.getTemplateName();
            if (templateName.equals(EmailTemplate.ACCOUNT_DELETION_TEMPLATE)) {
                emailToken.setDeleteDate(rs.getDate("deleteDate").toLocalDate());
            } else if (templateName.equals(EmailTemplate.ACCOUNT_UPDATE_TEMPLATE)) {
                emailToken.setUpdatedAt(rs.getTimestamp("updatedAt"));
                emailToken.setEmail(emailProperty.getOldEmail());
            }
        }

        return emailToken;
    }
}
