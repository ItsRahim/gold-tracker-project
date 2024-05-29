package com.rahim.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "kafka_unsent_messages")
public class KafkaUnsentMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "failed_time")
    private Instant failedTime;

    @Column(name = "topic")
    private String topic;

    @Column(name = "message_data")
    private String messageData;

    public KafkaUnsentMessage(Integer id, String topic, String messageData) {
        this.id = id;
        this.topic = topic;
        this.messageData = messageData;
    }
}