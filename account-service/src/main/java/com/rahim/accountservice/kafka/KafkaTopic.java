package com.rahim.accountservice.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Rahim Ahmed
 * @created 12/12/2023
 */
@Getter
@Component
@RefreshScope
public class KafkaTopic {

    private KafkaTopic() {}

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

    @Value("${topics.send-id-result}")
    private String sendIdResult;

}
