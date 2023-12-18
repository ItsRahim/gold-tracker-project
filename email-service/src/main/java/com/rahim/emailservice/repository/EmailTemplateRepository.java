package com.rahim.emailservice.repository;

import com.rahim.emailservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {
    @Query(value = "SELECT et.template_id FROM rgts.email_template et WHERE et.template_name = :name", nativeQuery = true)
    Integer findIdByTemplateName(String name);

    @Query(value = "SELECT unnest(et.placeholders) FROM rgts.email_template et WHERE et.template_id = :id", nativeQuery = true)
    List<String> findPlaceholdersByTemplateId(@Param("id") Integer id);
}
