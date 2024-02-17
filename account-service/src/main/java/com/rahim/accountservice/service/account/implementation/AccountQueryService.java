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

/**
 * This service class is responsible for querying account information.
 * It implements the IAccountQueryService interface.
 */
@Service
@RequiredArgsConstructor
public class AccountQueryService implements IAccountQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountQueryService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IKafkaService kafkaService;
    private final TopicConstants topicConstants;

    /**
     * @see IAccountQueryService
     */
    @Override
    public void existsById(String userId) {
        boolean found = false;

        if (userId != null && !userId.isEmpty()) {
            try {
                int id = Integer.parseInt(userId);
                found = accountRepositoryHandler.findById(id).isPresent();
            } catch (NumberFormatException e){
                LOG.error("Error parsing [{}] to an integer. Cannot find user", userId);
            }
        } else {
            LOG.warn("User ID is null or empty");
        }

        kafkaService.sendMessage(topicConstants.getSendIdResult(), String.valueOf(found));
    }

    /**
     * @see IAccountQueryService
     */
    @Override
    public List<Account> getAllAccounts() {
        return accountRepositoryHandler.getAllAccounts();
    }

    /**
     * @see IAccountQueryService
     */
    @Override
    public Optional<Account> findAccountById(int accountId) {
        return accountRepositoryHandler.findById(accountId);
    }

}
