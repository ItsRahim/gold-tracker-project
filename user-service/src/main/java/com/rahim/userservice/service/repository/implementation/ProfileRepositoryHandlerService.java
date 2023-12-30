package com.rahim.userservice.service.repository.implementation;

import com.rahim.userservice.repository.ProfileRepository;
import com.rahim.userservice.service.repository.IProfileRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileRepositoryHandlerService implements IProfileRepositoryHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProfileRepositoryHandlerService.class);
    private final ProfileRepository profileRepository;

}
