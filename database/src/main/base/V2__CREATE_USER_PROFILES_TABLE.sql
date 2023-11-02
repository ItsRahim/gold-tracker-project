CREATE TABLE rgts.user_profiles (
    profile_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES rgts.users(user_id),
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    contact_number VARCHAR(20),
    address TEXT
);
ALTER TABLE rgts.user_profiles
    ADD CONSTRAINT fk_user_profiles_user_id
        FOREIGN KEY (user_id)
            REFERENCES rgts.users(user_id)
            ON DELETE CASCADE;

CREATE OR REPLACE FUNCTION rgts.audit_user_changes()
RETURNS TRIGGER AS $$
BEGIN
INSERT INTO audit_log (user_id, action, change_timestamp, old_data, new_data)
VALUES (NEW.user_id, TG_OP, NOW(), ROW(OLD.*), ROW(NEW.*));

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER user_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON rgts.users
    FOR EACH ROW
    EXECUTE FUNCTION rgts.audit_user_changes();