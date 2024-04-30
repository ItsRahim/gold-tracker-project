package com.rahim.notificationservice.kafka;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KafkaTopic {

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

    @Value("${topics.check-user-id}")
    private String checkIdExists;

}
