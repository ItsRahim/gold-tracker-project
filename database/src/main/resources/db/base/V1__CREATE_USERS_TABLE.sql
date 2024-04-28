CREATE SCHEMA IF NOT EXISTS rgts;

CREATE TABLE rgts.user_accounts (
    account_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    account_status VARCHAR(255) DEFAULT 'ACTIVE' NOT NULL,
    account_locked BOOLEAN DEFAULT false,
    credentials_expired BOOLEAN DEFAULT false,
    last_login TIMESTAMP(0) NULL,
    notification_setting BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    login_attempts INT DEFAULT 0,
    delete_date DATE DEFAULT NULL
);

COMMENT ON TABLE rgts.user_accounts IS 'The account accounts table';
COMMENT ON COLUMN rgts.user_accounts.account_id IS 'The account ID';
COMMENT ON COLUMN rgts.user_accounts.email IS 'Unique account email';
COMMENT ON COLUMN rgts.user_accounts.password_hash IS 'Hashed account password';
COMMENT ON COLUMN rgts.user_accounts.account_status IS 'User account status';
COMMENT ON COLUMN rgts.user_accounts.account_locked IS 'Is the account locked?';
COMMENT ON COLUMN rgts.user_accounts.credentials_expired IS 'Are the credentials expired?';
COMMENT ON COLUMN rgts.user_accounts.last_login IS 'Last login timestamp';
COMMENT ON COLUMN rgts.user_accounts.notification_setting IS 'Notification settings';
COMMENT ON COLUMN rgts.user_accounts.created_at IS 'User account creation time';
COMMENT ON COLUMN rgts.user_accounts.updated_at IS 'Last update time';
COMMENT ON COLUMN rgts.user_accounts.login_attempts IS 'Number of login attempts';
COMMENT ON COLUMN rgts.user_accounts.delete_date IS 'Deletion date of account account and profile';