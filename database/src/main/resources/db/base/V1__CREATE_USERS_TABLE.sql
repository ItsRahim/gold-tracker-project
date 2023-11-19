CREATE SCHEMA IF NOT EXISTS rgts;

CREATE TABLE rgts.users (
    user_id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    account_status VARCHAR(255) DEFAULT 'ACTIVE' NOT NULL,
    account_locked BOOLEAN DEFAULT false,
    credentials_expired BOOLEAN DEFAULT false,
    last_login TIMESTAMP(0) NULL,
    notification_setting BOOLEAN DEFAULT false,
    created_at TIMESTAMPTZ(0) DEFAULT NOW(),
    updated_at TIMESTAMPTZ(0) DEFAULT NOW(),
    login_attempts INT DEFAULT 0,
    delete_date DATE DEFAULT NULL
);

COMMENT ON TABLE rgts.users IS 'The user accounts table';
COMMENT ON COLUMN rgts.users.user_id IS 'The user ID';
COMMENT ON COLUMN rgts.users.email IS 'Unique user email';
COMMENT ON COLUMN rgts.users.password_hash IS 'Hashed user password';
COMMENT ON COLUMN rgts.users.account_status IS 'User account status';
COMMENT ON COLUMN rgts.users.account_locked IS 'Is the account locked?';
COMMENT ON COLUMN rgts.users.credentials_expired IS 'Are the credentials expired?';
COMMENT ON COLUMN rgts.users.last_login IS 'Last login timestamp';
COMMENT ON COLUMN rgts.users.notification_setting IS 'Notification settings';
COMMENT ON COLUMN rgts.users.created_at IS 'User account creation time';
COMMENT ON COLUMN rgts.users.updated_at IS 'Last update time';
COMMENT ON COLUMN rgts.users.login_attempts IS 'Number of login attempts';
COMMENT ON COLUMN rgts.users.delete_date IS 'Deletion date of user account and profile';