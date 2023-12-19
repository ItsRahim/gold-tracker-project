CREATE TABLE rgts.threshold_alerts (
    alert_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES rgts.users(user_id) ON DELETE CASCADE,
    threshold_price NUMERIC(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT true
);

CREATE INDEX idx_user_id ON rgts.threshold_alerts(user_id);

COMMENT ON TABLE rgts.threshold_alerts is 'Table storing price alerts for users';
COMMENT ON COLUMN rgts.threshold_alerts.alert_id IS 'The unique ID for each alert';
COMMENT ON COLUMN rgts.threshold_alerts.user_id IS 'The user ID which set the alert';
COMMENT ON COLUMN rgts.threshold_alerts.threshold_price IS 'The price at which the user wants to be notified';
COMMENT ON COLUMN rgts.threshold_alerts.is_active IS 'The flag indicating if the alert is active';