package com.rahim.emailservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "email_history", schema = "rgts")
public class EmailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "retention_date", nullable = false)
    private LocalDate retentionDate;

}