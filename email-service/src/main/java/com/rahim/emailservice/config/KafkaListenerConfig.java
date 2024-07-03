package com.rahim.emailservice.config;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.model.kafka.AccountEmailData;
import com.rahim.common.model.kafka.PriceAlertEmailData;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.KafkaUtil;
import com.rahim.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import static com.rahim.common.util.JsonUtil.convertJsonToObject;

/**
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final EmailService emailService;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.SEND_ACCOUNT_ALERT, groupId = "group2")
    void accountAlertEmail(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                           ConsumerRecord<String, String> consumerRecord,
                           Acknowledgment acknowledgment) {

        KafkaUtil.logKafkaMessage(message, key, consumerRecord, ts);

        if (messageManager.isProcessed(key)) {
            log.debug("Message '{}' has already been processed. Not processing account alert update", message);
            acknowledgment.acknowledge();
            return;
        }

        AccountEmailData accountEmailData = convertJsonToObject(message, AccountEmailData.class);
        emailService.sendAccountAlert(accountEmailData);
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = KafkaTopic.SEND_PRICE_ALERT, groupId = "group2")
    void priceAlertEmail(@Payload String message,
                           @Header(KafkaHeaders.RECEIVED_KEY) String key,
                           @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                           ConsumerRecord<String, String> consumerRecord,
                           Acknowledgment acknowledgment) {

        KafkaUtil.logKafkaMessage(message, key, consumerRecord, ts);

        if (messageManager.isProcessed(key)) {
            log.debug("Message '{}' has already been processed. Not sending price alert email.", message);
            acknowledgment.acknowledge();
            return;
        }

        PriceAlertEmailData priceAlertData = convertJsonToObject(message, PriceAlertEmailData.class);
        emailService.sendPriceAlert(priceAlertData);
        acknowledgment.acknowledge();
    }

}
