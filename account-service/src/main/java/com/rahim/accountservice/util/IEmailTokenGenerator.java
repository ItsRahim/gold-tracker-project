package com.rahim.accountservice.util;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IEmailTokenGenerator {

    /**
     * Generates email tokens i.e. values for fields to be populated in an email template, for a given account.
     *
     * @param templateName The name of the email template.
     * @param accountId The ID of the account for which to generate the tokens.
     * @param includeUsername A flag indicating whether to include the username in the tokens.
     * @param includeDate A flag indicating whether to include the date in the tokens.
     * @param oldEmail An optional parameter that, if provided, will replace the current email in the tokens when the templateName is "Account Update".
     */
    void generateEmailTokens(String templateName, int accountId, boolean includeUsername, boolean includeDate, String... oldEmail);
}
