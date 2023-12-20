package com.rahim.pricingservice.constant;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TopicConstants {

    @Value("${topics.add-gold-type}")
    private String addGoldTypeTopic;

    @Value("${topics.delete-gold-type}")
    private String deleteGoldTypeTopic;

    @Value("${topics.send-notification-price}")
    private String sendNotificationPriceTopic;

    @Value("${topics.update-price-history}")
    private String updatePriceHistoryTopic;

    @Value("${topics.update-gold-price-job}")
    private String updateGoldPriceJobTopic;

    @Value("${topics.custom-api-data}")
    private String customApiDataTopic;

}
