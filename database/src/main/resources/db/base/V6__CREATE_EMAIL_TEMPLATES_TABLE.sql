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
VALUES ('Account Deletion',
        'Account Deletion Warning',
        'Dear @username,' ||
        'We hope this message finds you well. This is a notification regarding your account at our service.' ||
        'We want to inform you that based on our records, your account is scheduled for deletion in 30 days, on @deletionDate. If you have any concerns or questions, please contact our support team as soon as possible.' ||
        'We value your presence with us, and we''d be happy to assist you with any issues you may have. To prevent the deletion of your account, make sure to log in within the next 30 days, before @deletionDate' ||
        'Thank you for being a part of our community.',
        ARRAY['@username', '@deletionDate']);

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES('Account Inactivity',
       'Account Inactivity Alert',
       'Dear @username' ||
       'We hope you are well. This is a notice regarding your account on our platform.' ||
       'Our records indicate that your account has been inactive for the past 30 days. To ensure the security of our system, we kindly request you to log in within the next 14 days. Failure to do so will result in an automatic request to delete your account.' ||
       'If you have any concerns or questions, please reach out to our support team. We value your continued presence and would like to assist you in any way we can.' ||
       'Thank you for being a part of our community.',
       ARRAY['@username']);

INSERT INTO rgts.email_template(template_name, subject, body, placeholders)
VALUES (
        'Account Deleted',
        'Account Deletion Confirmation',
        'Dear @username,' ||
        'We hope this message finds you well. This is to inform you that your account at our service has been successfully deleted.' ||
        'If you believe this is an error or if you have any questions, please contact our support team for further assistance.',
        ARRAY['@username']);