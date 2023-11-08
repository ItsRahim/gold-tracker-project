CREATE TABLE rgts.archived_users (
    archive_id SERIAL PRIMARY KEY,
    user_account_data JSONB,
    user_profile_data JSONB,
    archived_date DATE
);

CREATE OR REPLACE FUNCTION rgts.archive_user_account_and_profile()
    RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO rgts.archived_users (user_account_data, user_profile_data, archived_date)
    VALUES (
            (SELECT to_jsonb(u) FROM rgts.users u WHERE u.user_id = OLD.user_id),
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