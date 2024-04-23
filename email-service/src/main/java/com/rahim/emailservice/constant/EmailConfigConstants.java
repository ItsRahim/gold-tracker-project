package com.rahim.emailservice.constant;

/**
 * This class holds the constants for email configuration.
 * <p>
 * These constants are used to inject property values from the email-service properties file, in spring cloud server, into the EmailConfig class.
 * Each constant represents a key in the properties file.
 *
 * @author Rahim Ahmed
 * @created  19/02/2024
 */
public final class EmailConfigConstants {

    private EmailConfigConstants() {}

    /** The host of the mail server. */
    public static final String MAIL_HOST = "${spring.mail.host}";

    /** The port of the mail server. */
    public static final String MAIL_PORT = "${spring.mail.port}";

    /** The username of the mail server. */
    public static final String MAIL_USERNAME = "${spring.mail.username}";

    /** The password of the mail server. */
    public static final String MAIL_PASSWORD = "${spring.mail.password}";

    /** The SMTP authentication setting for the mail server. */
    public static final String MAIL_SMTP_AUTH = "${spring.mail.properties.mail.smtp.auth}";

    /** The SMTP startTLS enable setting for the mail server. */
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "${spring.mail.properties.mail.smtp.starttls.enable}";

}
