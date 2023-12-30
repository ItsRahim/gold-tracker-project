package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.service.account.IAccountCreation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountCreationService implements IAccountCreation {
    private static final Logger LOG = LoggerFactory.getLogger(AccountCreationService.class);
}
