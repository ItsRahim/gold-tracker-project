package com.rahim.emailservice.repository;

import com.rahim.emailservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is a repository interface for the Email Templates table.
 * It extends JpaRepository to provide methods for CRUD (Create, Read, Update, Delete) operations.
 * <p>
 * The repository provides methods to perform operations on EmailTemplate entity,
 * such as save, find, delete, etc. These operations are performed in the context
 * of managing Account entities in relation to their persistence.
 *
 * @param EmailTemplate - The entity type the repository manages.
 * @param Integer - The type of the entity's identifier.
 */
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {

    /**
     * This method retrieves the template id from a given template name
     *
     * @param templateName - The name of the template whose id is to be found
     * @return an integer of the template id
     */
    @Query(value = "SELECT et.template_id FROM rgts.email_template et WHERE et.template_name = :templateName", nativeQuery = true)
    Integer findIdByTemplateName(String templateName);

    /**
     * This method retrieves the list of placeholders in an email template given the template id
     *
     * @param id - The id of the template whose placeholders are being retrieved
     * @return list of type string containing all the placeholders
     */
    @Query(value = "SELECT unnest(et.placeholders) FROM rgts.email_template et WHERE et.template_id = :id", nativeQuery = true)
    List<String> findPlaceholdersByTemplateId(@Param("id") Integer id);

}
