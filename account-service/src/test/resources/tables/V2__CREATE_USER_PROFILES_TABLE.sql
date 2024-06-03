CREATE TABLE rgts.user_profiles (
    profile_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES rgts.user_accounts(account_id) ON DELETE CASCADE,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    contact_number VARCHAR(20),
    street VARCHAR(255),
    city VARCHAR(255),
    post_code VARCHAR(20),
    country VARCHAR(255)
);

COMMENT ON TABLE rgts.user_profiles IS 'The account profile table';
COMMENT ON COLUMN rgts.user_profiles.profile_id IS 'The profile ID';
COMMENT ON COLUMN rgts.user_profiles.account_id IS 'The account ID from rgts.accounts table';
COMMENT ON COLUMN rgts.user_profiles.username IS 'The username associated with the account profile';
COMMENT ON COLUMN rgts.user_profiles.first_name IS 'The first name of the account';
COMMENT ON COLUMN rgts.user_profiles.last_name IS 'The last name of the account';
COMMENT ON COLUMN rgts.user_profiles.contact_number IS 'The contact number of the account';
COMMENT ON COLUMN rgts.user_profiles.street IS 'The street address';
COMMENT ON COLUMN rgts.user_profiles.city IS 'The city';
COMMENT ON COLUMN rgts.user_profiles.post_code IS 'The postal code';
COMMENT ON COLUMN rgts.user_profiles.country IS 'The country';