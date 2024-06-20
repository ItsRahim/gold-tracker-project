package com.rahim.accountservice.util;

import com.rahim.accountservice.model.EmailProperty;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.accountservice.service.repository.IProfileRepositoryHandler;
import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.IKafkaService;
import com.rahim.common.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(EmailTokenGenerator.class);
    private final IProfileRepositoryHandler profileRepositoryHandler;
    private final IKafkaService kafkaService;

    public void generateEmailTokens(EmailProperty emailProperty) {
        try {
            log.debug("Generating email tokens");
            AccountEmailData emailToken = profileRepositoryHandler.generateEmailTokens(emailProperty);
            String emailData = JsonUtil.convertObjectToJson(emailToken);
            kafkaService.sendMessage(KafkaTopic.SEND_ACCOUNT_ALERT, emailData);
        } catch (Exception e) {
            log.error("An error occurred generating email tokens: {}", e.getMessage(), e);
            throw new RuntimeException("An error occurred generating email tokens", e);
        }
    }
}
