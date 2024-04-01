package com.rahim.pricingservice.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 20/12/2023
 */
@Getter
@Component
public final class TopicConstants {

    @Value("${topics.send-notification-price}")
    private String sendNotificationPriceTopic;

    @Value("${topics.update-price-history}")
    private String updatePriceHistoryTopic;

    @Value("${topics.update-gold-price-job}")
    private String updateGoldPriceJobTopic;

    @Value("${topics.custom-api-data}")
    private String customApiDataTopic;

}
