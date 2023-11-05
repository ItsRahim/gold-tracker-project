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

