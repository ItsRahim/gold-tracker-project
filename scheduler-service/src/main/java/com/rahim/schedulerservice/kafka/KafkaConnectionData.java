package com.rahim.schedulerservice.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConnectionData {

    @Value("${spring.kafka.bootstrap-servers}")
    public String SERVER_NAME;

    public static final String BYTE_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.ByteArraySerializer";
    public static final String STRING_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String BYTE_DESERIALIZER_CLASS = "org.apache.kafka.common.serialization.ByteArrayDeserializer";
    public static final String STRING_DESERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringDeserializer";

    public String getServerName() {
        return SERVER_NAME;
    }

    public String getByteSerialiserClass() {
        return BYTE_SERIALIZER_CLASS;
    }

    public String getStringSerialiserClass() {
        return STRING_SERIALIZER_CLASS;
    }

    public String getByteDeserialiserClass() {
        return BYTE_DESERIALIZER_CLASS;
    }

    public String getStringDeserialiserClass() {
        return STRING_DESERIALIZER_CLASS;
    }
}
