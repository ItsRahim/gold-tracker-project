package com.rahim.schedulerservice.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 24/04/2024
 */
@Getter
@Component
public class KafkaTopic {

    @Value("${topics.scheduler-user-cleanup}")
    private String cleanupTopic;

    @Value("${topics.scheduler-update-price}")
    private String updatePriceTopic;

    @Value("${topics.scheduler-update-price-history}")
    private String updatePriceHistoryTopic;
}
