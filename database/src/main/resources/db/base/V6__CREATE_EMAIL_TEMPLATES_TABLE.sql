CREATE TABLE rgts.email_template (
    template_id SERIAL PRIMARY KEY,
    template_name VARCHAR(255) NOT NULL,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    placeholders VARCHAR(255) [] NOT NULL
);