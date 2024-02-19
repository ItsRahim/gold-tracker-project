package com.rahim.emailservice.service;

/**
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
public interface IKafkaDataProcessor {

    /**
     * This method processes Kafka data.
     * It parses the data, extracts the necessary information, populates an email template, and sends an email.
     *
     * @param emailData the Kafka data to process
     * @throws JsonProcessingException if an error occurs while parsing the Kafka data
     */
    void processKafkaData(String emailData);
}
