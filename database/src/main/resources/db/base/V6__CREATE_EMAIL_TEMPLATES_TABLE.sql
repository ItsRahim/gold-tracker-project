CREATE TABLE rgts.email_template (
    template_id SERIAL PRIMARY KEY,
    template_name VARCHAR(255),
    subject VARCHAR(255),
    body TEXT,
    placeholders VARCHAR(255) [] NOT NULL
);