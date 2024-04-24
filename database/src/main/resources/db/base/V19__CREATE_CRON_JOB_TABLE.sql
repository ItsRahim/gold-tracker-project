CREATE TABLE rgts.cron_jobs (
    cj_id SERIAL PRIMARY KEY,
    cj_name VARCHAR(50) NOT NULL,
    cj_purpose TEXT,
    cj_schedule VARCHAR(100) NOT NULL,
    cj_microservice_affected VARCHAR(255)
);

COMMENT ON TABLE rgts.cron_jobs IS 'A table containing information about all cron jobs in the system';
COMMENT ON COLUMN rgts.cron_jobs.cj_id IS 'The unique ID for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_name IS 'The name for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_purpose IS 'The description about the purpose of the cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_schedule IS 'The cron expression or schedule time for each cron job';
COMMENT ON COLUMN rgts.cron_jobs.cj_microservice_affected IS 'The name or reference of the microservice affected by each cron job';
