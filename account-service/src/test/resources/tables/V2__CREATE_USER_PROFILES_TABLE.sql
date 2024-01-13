CREATE TABLE rgts.user_profiles (
    profile_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES rgts.user_accounts(account_id),
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    contact_number VARCHAR(20),
    address TEXT
);

ALTER TABLE rgts.user_profiles
    ADD CONSTRAINT fk_user_profiles_user_id
        FOREIGN KEY (account_id)
            REFERENCES rgts.user_accounts(account_id)
            ON DELETE CASCADE;


COMMENT ON TABLE rgts.user_profiles IS 'The account profile table';
COMMENT ON COLUMN rgts.user_profiles.profile_id IS 'The profile ID';
COMMENT ON COLUMN rgts.user_profiles.account_id IS 'The account ID from rgts.accounts table';
COMMENT ON COLUMN rgts.user_profiles.username IS 'The username associated with the account profile';
COMMENT ON COLUMN rgts.user_profiles.first_name IS 'The first name of the account';
COMMENT ON COLUMN rgts.user_profiles.last_name IS 'The last name of the account';
COMMENT ON COLUMN rgts.user_profiles.contact_number IS 'The contact number of the account';
COMMENT ON COLUMN rgts.user_profiles.address IS 'The address of the account';