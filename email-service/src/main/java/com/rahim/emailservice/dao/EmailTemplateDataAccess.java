package com.rahim.emailservice.dao;

/**
 * @author Rahim Ahmed
 * @created 26/04/2024
 */
public class EmailTemplateDataAccess {

    private EmailTemplateDataAccess() {}

    public static final String TABLE_NAME = "rgts.email_template";
    public static final String COL_TEMPLATE_ID = "template_id";
    public static final String COL_TEMPLATE_NAME = "template_name";
    public static final String COL_SUBJECT = "subject";
    public static final String COL_BODY = "body";
    public static final String COL_PLACEHOLDER = "placeholders";

}
