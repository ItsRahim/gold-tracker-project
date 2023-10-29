CREATE TABLE rgts.audit_log (
    log_id SERIAL PRIMARY KEY,
    user_id INT,
    action VARCHAR(10),
    change_timestamp TIMESTAMPTZ,
    old_data JSONB,
    new_data JSONB
);