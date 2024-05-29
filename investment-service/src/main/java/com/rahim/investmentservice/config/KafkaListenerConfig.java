package com.rahim.investmentservice.config;

import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.JsonUtil;
import com.rahim.common.util.KafkaKeyUtil;
import com.rahim.investmentservice.service.holding.HoldingUpdateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    @KafkaListener(topics = "UPDATE-TOPIC", groupId = "group2")
    public void someMethod(String message) {
        if (!messageManager.isProcessed(message)) {
            String priceData = KafkaKeyUtil.extractDataFromKey(message);
            Map<String, Object> data = JsonUtil.convertJsonToMap(priceData);
            Integer goldTypeId = (Integer) data.get("id");
            Double price = (Double) data.get("price");
            holdingUpdateService.updateCurrentValue(goldTypeId, price);
            messageManager.markAsProcessed(message);
        } else {
            LOG.debug("Message '{}' has already been processed. Skipping cleanup job.", message);
        }
    }

}