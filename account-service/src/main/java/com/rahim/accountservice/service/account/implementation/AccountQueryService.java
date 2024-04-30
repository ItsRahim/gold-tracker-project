package com.rahim.accountservice.service.account.implementation;

import com.rahim.accountservice.kafka.KafkaTopics;
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
 *
 * @author Rahim Ahmed
 * @created 30/12/2023
 */
@Service
@RequiredArgsConstructor
public class AccountQueryService implements IAccountQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(AccountQueryService.class);
    private final IAccountRepositoryHandler accountRepositoryHandler;
    private final IKafkaService kafkaService;
    private final KafkaTopics kafkaTopics;

    /**
     * @see IAccountQueryService
     */
    @Override
    public void checkNotificationCriteria(String userId) {
        if (userId == null || userId.isEmpty()) {
            LOG.warn("User ID is null or empty");
            return;
        }

        try {
            int id = Integer.parseInt(userId);
            Optional<Account> optionalAccount = accountRepositoryHandler.findById(id);
            if (optionalAccount.isPresent() && notificationIsEnabled(optionalAccount.get())) {
                kafkaService.sendMessage(kafkaTopics.getSendIdResult(), "true");
            } else {
                kafkaService.sendMessage(kafkaTopics.getSendIdResult(), "false");
            }
        } catch (NumberFormatException e) {
            LOG.error("Error parsing [{}] to an integer. Cannot find user", userId);
        }
    }

    private boolean notificationIsEnabled(Account account) {
        return account.getNotificationSetting();
    }

    /**
     * @see IAccountQueryService
     */
    @Override
    public List<Account> getAllAccounts() {
        return accountRepositoryHandler.getAllAccounts();
    }

}
