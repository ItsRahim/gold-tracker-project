CREATE TABLE rgts.cron_jobs (
    cj_id SERIAL PRIMARY KEY,
    cj_name VARCHAR(50) NOT NULL,
    cj_purpose TEXT,
    cj_schedule VARCHAR(100) NOT NULL,
    cj_microservice_affected VARCHAR(255),
    cj_is_active BOOLEAN DEFAULT TRUE
);

COMMENT ON TABLE rgts.cron_jobs IS 'A table containing information about all cron jobs in the system';
COMMENT ON COLUMN rgts.cron_jobs.cj_id IS 'The unique ID for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_name IS 'The name for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_purpose IS 'The description about the purpose of the cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_schedule IS 'The cron expression or schedule time for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_microservice_affected IS 'The name or reference of the microservice affected by each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_is_active IS 'A flag indicating if cron job is active';

-- User Cleanup Job: Run every 43200 seconds (12 hours)
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active)
VALUES ('User Cleanup Job', 'Cleanup inactive user accounts', '0 */720 * * * *', 'Account Service', TRUE);

-- Update Gold Price Job: Run every minute
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active)
VALUES ('Update Gold Price Job', 'Update gold prices', '0 * * * * *', 'Pricing Service', TRUE);

-- Update Gold Price History Job: Run daily at 23:59
INSERT INTO rgts.cron_jobs (cj_name, cj_purpose, cj_schedule, cj_microservice_affected, cj_is_active)
VALUES ('Update Gold Price History Job', 'Update gold price history', '0 59 23 * * *', 'Pricing Service', TRUE);