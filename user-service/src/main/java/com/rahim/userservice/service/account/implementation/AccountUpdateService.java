package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.service.account.IAccountUpdate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUpdateService implements IAccountUpdate {
    private static final Logger LOG = LoggerFactory.getLogger(AccountUpdateService.class);
}
