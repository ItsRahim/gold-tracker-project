CREATE TABLE rgts.threshold_alerts (
    alert_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES rgts.user_accounts(account_id) ON DELETE CASCADE,
    threshold_price NUMERIC(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT true NOT NULL
);

CREATE INDEX idx_user_id ON rgts.threshold_alerts(account_id);

COMMENT ON TABLE rgts.threshold_alerts is 'Table storing price alerts for accounts';
COMMENT ON COLUMN rgts.threshold_alerts.alert_id IS 'The unique ID for each alert';
COMMENT ON COLUMN rgts.threshold_alerts.account_id IS 'The account ID which set the alert';
COMMENT ON COLUMN rgts.threshold_alerts.threshold_price IS 'The price at which the account wants to be notified';
COMMENT ON COLUMN rgts.threshold_alerts.is_active IS 'The flag indicating if the alert is active';