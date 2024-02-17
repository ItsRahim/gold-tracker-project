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
     * This method checks if an account with the given user ID exists.
     * If the user ID is not null or empty, it tries to parse it to an integer and find the account with this ID.
     * If the account is found, it sends a message with the result to the Kafka topic.
     *
     * @param userId the ID of the user to be checked
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
     * This method retrieves all accounts.
     *
     * @return a list of all accounts
     */
    @Override
    public List<Account> getAllAccounts() {
        return accountRepositoryHandler.getAllAccounts();
    }

    /**
     * This method finds an account by its ID.
     *
     * @param accountId the ID of the account to be found
     * @return an Optional containing the found account, or an empty Optional if no account was found
     */
    @Override
    public Optional<Account> findAccountById(int accountId) {
        return accountRepositoryHandler.findById(accountId);
    }

}
