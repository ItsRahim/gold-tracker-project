package com.rahim.emailservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 26/11/2023
 */
@Getter
@Setter
@Entity
@Table(name = "email_template", schema = "rgts")
public class EmailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id", nullable = false)
    private Integer id;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "body", nullable = false, length = Integer.MAX_VALUE)
    private String body;

    @ElementCollection
    @Column(name = "placeholders", nullable = false)
    private List<String> placeholders;

}