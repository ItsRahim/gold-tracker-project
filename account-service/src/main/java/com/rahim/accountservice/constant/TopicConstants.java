package com.rahim.accountservice.constant;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class TopicConstants {

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

    @Value("${topics.send-id-result}")
    private String sendIdResult;

}
