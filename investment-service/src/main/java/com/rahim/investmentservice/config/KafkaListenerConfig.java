package com.rahim.investmentservice.config;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.model.kafka.GoldTypePrice;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.JsonUtil;
import com.rahim.common.util.KafkaUtil;
import com.rahim.investmentservice.service.holding.HoldingUpdateService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author Rahim Ahmed
 * @created 19/05/2024
 */
@Component
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final HoldingUpdateService holdingUpdateService;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.HOLDING_PRICE_UPDATE, groupId = "group2")
    void processUpdateTopicMessage(@Payload String message,
                                   @Header(KafkaHeaders.RECEIVED_KEY) String key,
                                   @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                                   ConsumerRecord<String, String> consumerRecord,
                                   Acknowledgment acknowledgment) {

        KafkaUtil.logReceivedMessage(message, key, consumerRecord, ts);

        if (messageManager.isProcessed(key)) {
            LOG.debug("Message '{}' has already been processed. Skipping.", message);
            acknowledgment.acknowledge();
            return;
        }

        handleNewMessage(message);
        messageManager.markAsProcessed(key);
        acknowledgment.acknowledge();
    }

    private void handleNewMessage(String priceData) {
        GoldTypePrice goldTypePrice = JsonUtil.convertJsonToObject(priceData, GoldTypePrice.class);

        Integer goldTypeId = Integer.valueOf(goldTypePrice.getId());
        BigDecimal price = goldTypePrice.getPrice();
        holdingUpdateService.updateCurrentValue(goldTypeId, price);
    }

}