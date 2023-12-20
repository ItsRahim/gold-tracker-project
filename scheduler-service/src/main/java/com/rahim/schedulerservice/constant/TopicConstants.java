package com.rahim.schedulerservice.constant;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TopicConstants {

    @Value("${topics.scheduler-user-cleanup}")
    private String cleanupTopic;

    @Value("${topics.scheduler-update-price}")
    private String updatePriceTopic;

    @Value("${topics.scheduler-update-price-history}")
    private String updatePriceHistoryTopic;
}
