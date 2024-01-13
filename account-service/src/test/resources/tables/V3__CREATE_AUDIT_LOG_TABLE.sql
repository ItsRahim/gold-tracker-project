CREATE TABLE rgts.audit_log (
    log_id SERIAL PRIMARY KEY,
    account_id INT,
    action VARCHAR(255),
    "table" VARCHAR(255),
    change_timestamp TIMESTAMPTZ(0),
    old_data JSONB,
    new_data JSONB
);

COMMENT ON TABLE rgts.audit_log IS 'The audit log table';
COMMENT ON COLUMN rgts.audit_log.log_id IS 'The unique log id';
COMMENT ON COLUMN rgts.audit_log.account_id IS 'The account id which performed the action recorded';
COMMENT ON COLUMN rgts.audit_log.action IS 'The type of CRUD operation performed by the account';
COMMENT ON COLUMN rgts.audit_log.table IS 'The table affected by the change';
COMMENT ON COLUMN rgts.audit_log.change_timestamp IS 'The time of which this changed occurred';
COMMENT ON COLUMN rgts.audit_log.old_data IS 'The old data before the change';
COMMENT ON COLUMN rgts.audit_log.new_data IS 'The new data after the change';