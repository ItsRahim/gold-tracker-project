package com.rahim.common.util;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahim Ahmed
 * @created 05/05/2024
 */
public class KafkaUtil {

    private KafkaUtil() {}

    private static final Logger LOG = LoggerFactory.getLogger(KafkaUtil.class);

    public static String extractDataFromKey(String message) {
        String[] parts = message.split(",", 2);

        if (parts.length < 2 || parts[1].isEmpty()) {
            return message;
        } else {
            return parts[1];
        }
    }

    public static String extractPriceData(String priceData) {
        int underscoreIndex = priceData.indexOf('_');

        if (underscoreIndex == -1 || underscoreIndex == priceData.length() - 1) {
            return null;
        }

        return priceData.substring(underscoreIndex + 1);
    }

    public static void logReceivedMessage(String message, String key, ConsumerRecord<String, String> consumerRecord, long ts) {
        LOG.debug("""
                        \n \n######### Message received #########
                        Received Message: {}
                        Received Key: {}
                        Received Topic: {}
                        Received Partition: {}
                        Received Offset: {}
                        Received Timestamp: {}
                        ######### End of Message #########
                        """,
                message, key, consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset(), ts);
    }
}
