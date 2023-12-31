CREATE TABLE rgts.archived_users (
    archive_id SERIAL PRIMARY KEY,
    user_account_data JSONB,
    user_profile_data JSONB,
    archived_date DATE
);

COMMENT ON TABLE rgts.archived_users IS 'The archived accounts table';
COMMENT ON COLUMN rgts.archived_users.archive_id IS 'Unique identifier for the archives';
COMMENT ON COLUMN rgts.archived_users.user_account_data IS 'The data associated with the deleted account account';
COMMENT ON COLUMN rgts.archived_users.user_profile_data IS 'The data associated with the deleted account profile';
COMMENT ON COLUMN rgts.archived_users.archived_date IS 'The date the account was archived/deleted';

CREATE OR REPLACE FUNCTION rgts.archive_user_account_and_profile()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO rgts.archived_users (user_account_data, user_profile_data, archived_date)
    VALUES (
            (SELECT to_jsonb(a) FROM rgts.user_accounts a WHERE a.account_id = OLD.account_id),
            (SELECT to_jsonb(OLD)),
            current_date
           );
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER archive_user_account_trigger
    AFTER DELETE ON rgts.user_profiles
    FOR EACH ROW
EXECUTE FUNCTION rgts.archive_user_account_and_profile();