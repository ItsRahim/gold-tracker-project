package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.service.account.IAccountDeletion;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDeletionService implements IAccountDeletion {
    private static final Logger LOG = LoggerFactory.getLogger(AccountDeletionService.class);
}
