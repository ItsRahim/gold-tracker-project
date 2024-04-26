package com.rahim.emailservice.repository;

import com.rahim.emailservice.dao.EmailTemplateDataAccess;
import com.rahim.emailservice.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rahim Ahmed
 * @created 26/11/2023
 */
@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Integer> {

    /**
     * This method retrieves the template id from a given template name
     *
     * @param templateName - The name of the template whose id is to be found
     * @return an integer of the template id
     */
    @Query(value = "SELECT "
            + EmailTemplateDataAccess.COL_TEMPLATE_ID
            + " FROM "
            + EmailTemplateDataAccess.TABLE_NAME
            + " WHERE "
            + EmailTemplateDataAccess.COL_TEMPLATE_NAME
            + " = :templateName", nativeQuery = true)
    Integer findIdByTemplateName(String templateName);

    /**
     * This method retrieves the list of placeholders in an email template given the template id
     *
     * @param id - The id of the template whose placeholders are being retrieved
     * @return list of type string containing all the placeholders
     */
    @Query(value = "SELECT UNNEST("
            + EmailTemplateDataAccess.COL_PLACEHOLDER + ")"
            + " FROM "
            + EmailTemplateDataAccess.TABLE_NAME
            + " WHERE "
            + EmailTemplateDataAccess.COL_TEMPLATE_ID
            + " = :id", nativeQuery = true)
    List<String> findPlaceholdersByTemplateId(@Param("id") Integer id);

}
