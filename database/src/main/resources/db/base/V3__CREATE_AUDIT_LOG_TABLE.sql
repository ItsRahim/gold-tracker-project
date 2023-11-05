CREATE TABLE rgts.audit_log (
    log_id SERIAL PRIMARY KEY,
    user_id INT,
    action VARCHAR(255),
    "table" VARCHAR(255),
    change_timestamp TIMESTAMPTZ(0),
    old_data VARCHAR(500),
    new_data VARCHAR(500)
);