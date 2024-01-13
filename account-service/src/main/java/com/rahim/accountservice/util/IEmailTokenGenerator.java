package com.rahim.accountservice.util;

public interface IEmailTokenGenerator {
    void generateEmailTokens(String templateName, int accountId, boolean includeUsername, boolean includeDate, String... oldEmail);
}
