-- Update updated_at trigger function
CREATE OR REPLACE FUNCTION rgts.update_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = (NOW() AT TIME ZONE 'UTC');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for update_updated_at function
CREATE TRIGGER update_updated_at_trigger
    BEFORE INSERT OR UPDATE ON rgts.user_accounts
    FOR EACH ROW
EXECUTE FUNCTION rgts.update_updated_at();

----------------------------------------

-- Capture user changes function
CREATE OR REPLACE FUNCTION rgts.capture_user_changes()
    RETURNS TRIGGER AS $$
BEGIN
    -- Inserting into audit_log table
    INSERT INTO rgts.audit_log (account_id, action, "table", change_timestamp, old_data, new_data)
    VALUES (NEW.account_id, TG_OP, TG_TABLE_NAME, (NOW() AT TIME ZONE 'UTC'), to_jsonb(OLD), to_jsonb(NEW));

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for capturing user changes
CREATE TRIGGER user_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON rgts.user_accounts
    FOR EACH ROW
EXECUTE FUNCTION rgts.capture_user_changes();

----------------------------------------

-- Update users' updated_at function
CREATE OR REPLACE FUNCTION rgts.update_users_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    -- Updating updated_at column
    UPDATE rgts.user_accounts
    SET updated_at = (NOW() AT TIME ZONE 'UTC')
    WHERE account_id = NEW.account_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for updating users' updated_at
CREATE TRIGGER update_users_updated_at_trigger
    AFTER UPDATE ON rgts.user_profiles
    FOR EACH ROW
EXECUTE FUNCTION rgts.update_users_updated_at();

----------------------------------------

-- Capture user profiles changes function
CREATE OR REPLACE FUNCTION rgts.capture_user_profiles_changes()
    RETURNS TRIGGER AS $$
BEGIN
    -- Inserting into audit_log table
    INSERT INTO rgts.audit_log (account_id, action, "table", change_timestamp, old_data, new_data)
    VALUES (NEW.account_id, TG_OP, TG_TABLE_NAME, (NOW() AT TIME ZONE 'UTC'), to_jsonb(OLD), to_jsonb(NEW));

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for capturing user profiles changes
CREATE TRIGGER user_profiles_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE ON rgts.user_profiles
    FOR EACH ROW
EXECUTE FUNCTION rgts.capture_user_profiles_changes();
