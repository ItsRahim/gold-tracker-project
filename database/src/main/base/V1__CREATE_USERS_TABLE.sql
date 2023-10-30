CREATE SCHEMA IF NOT EXISTS rgts;

CREATE TABLE rgts.users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    account_non_expired BOOLEAN DEFAULT true,
    account_non_locked BOOLEAN DEFAULT true,
    credentials_non_expired BOOLEAN DEFAULT true,
    last_login TIMESTAMP,
    notification_setting INT DEFAULT 1,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    login_attempts INT DEFAULT 0,
    to_delete BOOLEAN DEFAULT false,
    delete_date TIMESTAMPTZ DEFAULT NULL,
    account_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE'
);

CREATE OR REPLACE FUNCTION rgts.update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_updated_at_trigger
    BEFORE UPDATE ON rgts.users
    FOR EACH ROW
    EXECUTE FUNCTION rgts.update_updated_at();
