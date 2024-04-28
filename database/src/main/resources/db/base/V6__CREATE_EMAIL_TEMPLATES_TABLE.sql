CREATE TABLE rgts.email_template (
    template_id SERIAL PRIMARY KEY,
    template_name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    placeholders VARCHAR(255) [] NOT NULL
);

COMMENT ON TABLE rgts.email_template IS 'The email templates table';
COMMENT ON COLUMN rgts.email_template.template_id IS 'Unique identifier for the email templates';
COMMENT ON COLUMN rgts.email_template.template_name IS 'Email template name';
COMMENT ON COLUMN rgts.email_template.subject IS 'Email subject';
COMMENT ON COLUMN rgts.email_template.body IS 'Email body';
COMMENT ON COLUMN rgts.email_template.placeholders IS 'Placeholders in the email to dynamically update';

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
        'Account Deletion',
        'Account Deletion Warning',
        'Dear @firstName @lastName,\n' ||
        '\nWe hope this message finds you well. This is a notification regarding your account at our service.\n' ||
        'We want to inform you that based on our records, your account, @username, is scheduled for deletion in 30 days, on @deletionDate. If you have any concerns or questions, please contact our support team as soon as possible.\n' ||
        'We value your presence with us, and we''d be happy to assist you with any issues you may have. To prevent the deletion of your account, make sure to log in within the next 30 days, before @deletionDate.\n' ||
        '\nThank you for being a part of our community.',
        ARRAY['@username', '@firstName', '@lastName', '@deletionDate']
       );

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
        'Account Inactivity',
        'Account Inactivity Alert',
        'Dear @firstName @lastName,\n' ||
        '\nWe hope you are well. This is a notice regarding your account on our platform.\n' ||
        'Our records indicate that your account has been inactive for the past 30 days. To ensure the security of our system, we kindly request you to log in within the next 14 days. Failure to do so will result in an automatic request to delete your account.\n' ||
        'If you have any concerns or questions, please reach out to our support team. We value your continued presence and would like to assist you in any way we can.\n' ||
        '\nThank you for being a part of our community.',
        ARRAY['@firstName', '@lastName']
       );

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
        'Account Deleted',
        'Account Deletion Confirmation',
        'Dear @firstName @lastName,\n' ||
        '\nWe hope this message finds you well. This is to inform you that your account, @username, at our service has been successfully deleted.\n' ||
        '\nIf you believe this is an error or if you have any questions, please contact our support team for further assistance.',
        ARRAY['@firstName', '@lastName', '@username']
       );

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
           'Account Update',
           'Account Update Alert',
           'Dear @firstName @lastName,\n' ||
           '\nWe hope this message finds you well. This is to inform you that your account information has been updated successfully.\n' ||
           '\nPlease review the changes below:\n\n' ||
           'Updated Username: @username\n' ||
           'Last Update Time: @updatedAt\n\n' ||
           'If you have any questions or concerns, please contact our support team for further assistance.\n\n' ||
           'Best regards,\nYour Service Team',
           ARRAY['@username', '@firstName', '@lastName', '@updatedAt']
       );