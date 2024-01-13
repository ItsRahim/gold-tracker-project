package com.rahim.accountservice.service.profile;

import java.util.Map;

public interface IProfileUpdateService {
    void updateProfile(int profileId, Map<String, String> updatedData);
}
