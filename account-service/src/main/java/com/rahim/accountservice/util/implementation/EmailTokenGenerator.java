package com.rahim.accountservice.util.implementation;

import com.rahim.accountservice.service.profile.IProfileQueryService;
import com.rahim.accountservice.util.IEmailTokenGenerator;
import com.rahim.accountservice.util.IEmailTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
@Component
@RequiredArgsConstructor
public class EmailTokenGenerator implements IEmailTokenGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTokenGenerator.class);
    private final IEmailTokenService emailTokenService;
    private final IProfileQueryService profileQueryService;

    @Override
    public void generateEmailTokens(String templateName, int accountId, boolean includeUsername, boolean includeDate, String... oldEmail) {
        try {
            Map<String, Object> emailData = profileQueryService.getProfileDetails(accountId);
            Map<String, Object> mutableData = new HashMap<>(emailData);

            if(templateName.equals("Account Update") && (oldEmail.length > 0)) {
                mutableData.put("email", oldEmail[0]);
            }

            emailTokenService.generateEmailTokens(mutableData, templateName, includeUsername, includeDate);
        } catch (Exception e) {
            LOG.error("Error generating email tokens for user ID {}: {}", accountId, e.getMessage(), e);
            throw new RuntimeException("Unexpected error", e);
        }
    }
}
