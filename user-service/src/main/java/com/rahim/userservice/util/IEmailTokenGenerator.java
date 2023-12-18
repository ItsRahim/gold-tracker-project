package com.rahim.userservice.util;

public interface IEmailTokenGenerator {
    void generateEmailTokens(String templateName, int userId, boolean includeUsername, boolean includeDate);
}
