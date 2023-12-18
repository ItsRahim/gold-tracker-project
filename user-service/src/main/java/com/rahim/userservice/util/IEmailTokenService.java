package com.rahim.userservice.util;

import java.util.Map;

public interface IEmailTokenService {
    void generateEmailTokens(Map<String, Object> emailData, String templateName, boolean includeUsername, boolean includeDate);
}
