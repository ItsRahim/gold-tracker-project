CREATE TABLE rgts.email_history (
    id SERIAL PRIMARY KEY,
    email_template_id INT REFERENCES rgts.email_template(template_id),
    recipient_email VARCHAR(255) NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    retention_date DATE NOT NULL
);