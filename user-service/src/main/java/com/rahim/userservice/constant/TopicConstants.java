package com.rahim.userservice.constant;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TopicConstants {

    @Value("${topics.send-email-topic}")
    private String sendEmailTopic;

}
