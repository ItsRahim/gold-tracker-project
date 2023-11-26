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


COMMENT ON TABLE rgts.user_profiles IS 'The user profile table';
COMMENT ON COLUMN rgts.user_profiles.profile_id IS 'The profile ID';
COMMENT ON COLUMN rgts.user_profiles.user_id IS 'The user ID from rgts.users table';
COMMENT ON COLUMN rgts.user_profiles.username IS 'The username associated with the user profile';
COMMENT ON COLUMN rgts.user_profiles.first_name IS 'The first name of the user';
COMMENT ON COLUMN rgts.user_profiles.last_name IS 'The last name of the user';
COMMENT ON COLUMN rgts.user_profiles.contact_number IS 'The contact number of the user';
COMMENT ON COLUMN rgts.user_profiles.address IS 'The address of the user';