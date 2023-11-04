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

CREATE OR REPLACE FUNCTION rgts.update_users_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE rgts.users
    SET updated_at = NOW()
    WHERE user_id = NEW.user_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_users_updated_at_trigger
    AFTER UPDATE ON rgts.user_profiles
    FOR EACH ROW
    EXECUTE FUNCTION rgts.update_users_updated_at();