package com.rahim.userservice.util.implementation;

import com.rahim.userservice.service.IUserProfileService;
import com.rahim.userservice.util.IEmailTokenGenerator;
import com.rahim.userservice.util.IEmailTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailTokenGenerator implements IEmailTokenGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(EmailTokenGenerator.class);
    private final IEmailTokenService emailTokenService;
    private final IUserProfileService userProfileService;

    @Override
    public void generateEmailTokens(String templateName, int userId, boolean includeUsername, boolean includeDate, String... oldEmail) {
        try {
            Map<String, Object> emailData = userProfileService.getUserProfileDetails(userId);
            Map<String, Object> mutableData = new HashMap<>(emailData);

            if(templateName.equals("Account Update") && (oldEmail.length > 0)) {
                mutableData.put("email", oldEmail[0]);
            }

            if (mutableData != null) {
                emailTokenService.generateEmailTokens(mutableData, templateName, includeUsername, includeDate);
            } else {
                LOG.info("No email data found for user ID {}", userId);
            }
        } catch (Exception e) {
            LOG.error("Error generating email tokens for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }
}
