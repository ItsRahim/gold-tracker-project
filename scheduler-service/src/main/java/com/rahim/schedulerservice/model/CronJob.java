package com.rahim.schedulerservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public CronJob(String name, String purpose, String schedule, String microserviceAffected, Boolean isActive) {
        this.name = name;
        this.purpose = purpose;
        this.schedule = schedule;
        this.microserviceAffected = microserviceAffected;
        this.isActive = isActive;
    }
}