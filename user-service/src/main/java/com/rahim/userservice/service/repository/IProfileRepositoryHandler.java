package com.rahim.userservice.service.repository;

import com.rahim.userservice.model.EmailProperty;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.userservice.entity.Profile;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
public interface IProfileRepositoryHandler {
    void createNewProfile(Profile profile);
    void updateProfile(Profile profile);
    void deleteProfile(int profileId);
    Profile findById(int profileId);
    boolean existsByUsername(String username);
    AccountEmailData generateEmailTokens(EmailProperty emailProperty);
    Profile getProfileByUsername(String username);
    int getProfileIdByAccountId(int accountId);
    List<Profile> getAllProfiles();
}
