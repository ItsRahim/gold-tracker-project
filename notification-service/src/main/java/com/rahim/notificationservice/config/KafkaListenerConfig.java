package com.rahim.notificationservice.config;

import com.rahim.common.constant.KafkaTopic;
import com.rahim.common.service.kafka.MessageManager;
import com.rahim.common.util.KafkaUtil;
import com.rahim.notificationservice.service.kafka.IKafkaDataProcessor;
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

import static com.rahim.common.util.KafkaUtil.extractDataFromKey;
import static com.rahim.common.util.KafkaUtil.logKafkaMessage;

@Configuration
@RequiredArgsConstructor
public class KafkaListenerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaListenerConfig.class);
    private final IKafkaDataProcessor kafkaDataProcessor;
    private final MessageManager messageManager;

    @KafkaListener(topics = KafkaTopic.THRESHOLD_PRICE_UPDATE, groupId = "group2")
    void priceListener(@Payload String priceData,
                       @Header(KafkaHeaders.RECEIVED_KEY) String key,
                       @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                       ConsumerRecord<String, String> consumerRecord,
                       Acknowledgment acknowledgment) {

        logKafkaMessage(priceData, key, consumerRecord, ts);

        if (messageManager.isProcessed(key)) {
            log.debug("Message '{}' has already been processed. Skipping sending email notification event", priceData);
            acknowledgment.acknowledge();
            return;
        }

        String kafkaData = extractDataFromKey(priceData);
        kafkaDataProcessor.processKafkaData(kafkaData);
        messageManager.markAsProcessed(key);
        acknowledgment.acknowledge();
    }
}
