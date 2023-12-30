package com.rahim.userservice.service.account.implementation;

import com.rahim.userservice.service.account.IAccountQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountQueryService implements IAccountQuery {
    private static final Logger LOG = LoggerFactory.getLogger(AccountQueryService.class);

}
