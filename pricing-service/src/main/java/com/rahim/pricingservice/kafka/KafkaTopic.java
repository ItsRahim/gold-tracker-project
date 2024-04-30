package com.rahim.pricingservice.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 20/12/2023
 */
@Getter
@Component
@RefreshScope
public final class KafkaTopic {

    private KafkaTopic() {}

    @Value("${topics.send-notification-price}")
    private String sendNotificationPriceTopic;

    @Value("${topics.update-price-history}")
    private String updatePriceHistoryTopic;

    @Value("${topics.update-gold-price-job}")
    private String updateGoldPriceJobTopic;

    @Value("${topics.custom-api-data}")
    private String customApiDataTopic;

}
