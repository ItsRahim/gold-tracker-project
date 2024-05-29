package com.rahim.schedulerservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cron_jobs")
public class CronJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cj_id", nullable = false)
    private Integer id;

    @Column(name = "cj_name", nullable = false, length = 50)
    private String name;

    @Column(name = "cj_purpose", length = Integer.MAX_VALUE)
    private String purpose;

    @Column(name = "cj_schedule", nullable = false, length = 100)
    private String schedule;

    @Column(name = "cj_microservice_affected")
    private String microserviceAffected;

    @Column(name = "cj_is_active")
    private Boolean isActive;

    @Column(name = "cj_last_update")
    private Instant lastUpdated;

    public CronJob(String name, String schedule) {
        this.name = name;
        this.schedule = schedule;
    }
}