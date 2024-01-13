package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.constant.TopicConstants;
import com.rahim.accountservice.exception.UserNotFoundException;
import com.rahim.accountservice.kafka.IKafkaService;
import com.rahim.accountservice.model.Account;
import com.rahim.accountservice.service.account.IAccountQueryService;
import com.rahim.accountservice.service.repository.IAccountRepositoryHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountQueryService implements IAccountQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(AccountQueryService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    @Override
    public void existsById(String userId) {
        int id = Integer.parseInt(userId);
        String found = String.valueOf(accountRepositoryHandler.findById(id).isPresent());
        kafkaService.sendMessage(topicConstants.getSendIdResult(), found);
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = accountRepositoryHandler.getAllAccounts();

        if (!accounts.isEmpty()) {
            LOG.info("Found {} accounts in the database", accounts.size());
        } else {
            LOG.info("No accounts found in the database");
        }

        return accounts;
    }

    @Override
    public Optional<Account> findAccountById(int accountId) {
        try {
            return accountRepositoryHandler.findById(accountId);
        } catch (Exception e) {
            LOG.error("Error while finding a user with ID: {}", accountId, e);
            throw new UserNotFoundException("Error finding a user by ID");
        }
    }
}
