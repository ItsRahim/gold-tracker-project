CREATE TABLE rgts.audit_log (
    log_id SERIAL PRIMARY KEY,
    user_id INT,
    action VARCHAR(10),
    change_timestamp TIMESTAMPTZ(0),
    old_data JSONB,
    new_data JSONB
);

CREATE OR REPLACE FUNCTION rgts.capture_user_changes()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO rgts.audit_log (user_id, action, change_timestamp, old_data, new_data)
    VALUES (NEW.user_id, TG_OP, NOW(), to_jsonb(OLD), to_jsonb(NEW));

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER user_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON rgts.users
    FOR EACH ROW
    EXECUTE FUNCTION rgts.capture_user_changes();

CREATE OR REPLACE FUNCTION rgts.capture_user_profiles_changes()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO rgts.audit_log (user_id, action, change_timestamp, old_data, new_data)
    VALUES (NEW.user_id, TG_OP, NOW(), to_jsonb(OLD), to_jsonb(NEW));

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER user_profiles_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON rgts.user_profiles
    FOR EACH ROW
    EXECUTE FUNCTION rgts.capture_user_profiles_changes();
