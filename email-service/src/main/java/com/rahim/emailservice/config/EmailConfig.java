package com.rahim.emailservice.config;

import com.rahim.emailservice.constant.EmailConfigConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * This class is a Spring configuration class for setting up the JavaMailSender bean.
 * <p>
 * It uses the constants from the {@code EmailConfigConstants} class to inject property values from the application properties file.
 * The {@code @Value} annotation is used to inject these property values into the fields of this class.
 * <p>
 * The {@code javaMailSender} method is a {@code @Bean} method that creates and configures a {@code JavaMailSenderImpl} instance.
 * This instance is configured with the host, port, username, and password for the mail server, as well as SMTP settings.
 *
 * @author Rahim Ahmed
 * @created 18/12/2023
 */
@Configuration
public class EmailConfig {

    @Value(EmailConfigConstants.MAIL_HOST)
    private String host;

    @Value(EmailConfigConstants.MAIL_PORT)
    private int port;

    @Value(EmailConfigConstants.MAIL_USERNAME)
    private String username;

    @Value(EmailConfigConstants.MAIL_PASSWORD)
    private String password;

    @Value(EmailConfigConstants.MAIL_SMTP_AUTH)
    private String smtpAuth;

    @Value(EmailConfigConstants.MAIL_SMTP_STARTTLS_ENABLE)
    private String startTlsEnable;

    /**
     * This method creates and configures a {@code JavaMailSenderImpl} instance.
     * <p>
     * The instance is configured with the host, port, username, and password for the mail server, as well as SMTP settings.
     * These settings are injected into this class from the application properties file using the {@code @Value} annotation.
     *
     * @return a configured {@code JavaMailSenderImpl} instance
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        mailSender.getJavaMailProperties().put("mail.smtp.auth", smtpAuth);
        mailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", startTlsEnable);

        return mailSender;
    }

}
