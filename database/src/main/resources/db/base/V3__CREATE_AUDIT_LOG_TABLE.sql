CREATE TABLE rgts.audit_log (
    log_id SERIAL PRIMARY KEY,
    user_id INT,
    action VARCHAR(255),
    "table" VARCHAR(255),
    change_timestamp TIMESTAMPTZ(0),
    old_data JSONB,
    new_data JSONB
);