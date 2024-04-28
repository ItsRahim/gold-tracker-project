package com.rahim.accountservice.util;

import com.rahim.accountservice.model.EmailProperty;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IEmailTokenService {

    /**
     * Generates email tokens for a given email data map and sends the tokens as a message to a Kafka topic.
     *
     * @param emailData The original map containing the email data.
     * @param templateName The name of the email template.
     * @param includeUsername A flag indicating whether to include the username in the tokens.
     * @param includeDate A flag indicating whether to include the date in the tokens.
     * @throws RuntimeException If an error occurs while generating the tokens or sending the message.
     */
    void generateEmailTokens(EmailProperty emailProperty);
}
