package com.rahim.userservice.service.profile;

import com.rahim.userservice.model.Profile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProfileQueryService {
    boolean existsByUsername(String username);
    Map<String, Object> getProfileDetails(int profileId);
    Optional<Profile> getProfileByUsername(String username);
    List<Profile> getAllProfiles();
}
