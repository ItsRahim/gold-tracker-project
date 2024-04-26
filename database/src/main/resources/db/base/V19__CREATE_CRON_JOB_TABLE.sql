CREATE TABLE rgts.cron_jobs (
    cj_id SERIAL PRIMARY KEY,
    cj_name VARCHAR(50) NOT NULL,
    cj_purpose TEXT,
    cj_schedule VARCHAR(100) NOT NULL,
    cj_microservice_affected VARCHAR(255),
    cj_is_active BOOLEAN DEFAULT TRUE,
    cj_last_updated TIMESTAMP WITH TIME ZONE
);

COMMENT ON TABLE rgts.cron_jobs IS 'A table containing information about all cron jobs in the system';
COMMENT ON COLUMN rgts.cron_jobs.cj_id IS 'The unique ID for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_name IS 'The name for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_purpose IS 'The description about the purpose of the cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_schedule IS 'The cron expression or schedule time for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_microservice_affected IS 'The name or reference of the microservice affected by each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_is_active IS 'A flag indicating if cron job is active';
COMMENT ON COLUMN rgts.cron_jobs.cj_last_updated IS 'A timestamp indicating when the field was last updated';

-- Create trigger function to update cj_last_updated
CREATE OR REPLACE FUNCTION update_cron_jobs_last_updated()
RETURNS TRIGGER AS $$
BEGIN
    new.cj_last_updated := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to call the function before update
CREATE TRIGGER cron_jobs_last_updated_trigger
BEFORE UPDATE ON rgts.cron_jobs
FOR EACH ROW EXECUTE FUNCTION update_cron_jobs_last_updated();

-- User Cleanup Job: Run every 43200 seconds (12 hours)
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active, cj_last_updated)
VALUES ('User Cleanup Job', 'Cleanup inactive user accounts', '0 */720 * * * *', 'Account Service', TRUE, CURRENT_TIMESTAMP);

-- Update Gold Price Job: Run every minute
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active, cj_last_updated)
VALUES ('Update Gold Price Job', 'Update gold prices', '0 * * * * *', 'Pricing Service', TRUE, CURRENT_TIMESTAMP);

-- Update Gold Price History Job: Run daily at 23:59
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active, cj_last_updatede)
VALUES ('Update Gold Price History Job', 'Update gold price history', '0 59 23 * * *', 'Pricing Service', TRUE, CURRENT_TIMESTAMP);