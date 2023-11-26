CREATE TABLE rgts.email_history (
    id SERIAL PRIMARY KEY,
    email_template_id INT REFERENCES rgts.email_template(template_id),
    recipient_email VARCHAR(255) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    retention_date DATE NOT NULL
);

COMMENT ON TABLE rgts.email_history IS 'The email history table';
COMMENT ON COLUMN rgts.email_history.id IS 'The unique identifier for the email history table';
COMMENT ON COLUMN rgts.email_history.email_template_id IS 'The foreign key associated to the email template sent';
COMMENT ON COLUMN rgts.email_history.recipient_email IS 'The email address the email was sent to';
COMMENT ON COLUMN rgts.email_history.sent_at IS 'The date the email was sent';
COMMENT ON COLUMN rgts.email_history.sent_at IS 'The status of the email';
COMMENT ON COLUMN rgts.email_history.retention_date IS 'The date the record of the email being sent will be deleted';