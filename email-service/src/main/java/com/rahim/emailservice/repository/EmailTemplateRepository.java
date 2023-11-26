package com.rahim.emailservice.repository;

import com.rahim.emailservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {
}
