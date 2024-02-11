CREATE TABLE rgts.processed_files (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    processing_status VARCHAR(50) NOT NULL,
    processed_at TIMESTAMPTZ DEFAULT NOW()
);

COMMENT ON TABLE rgts.processed_files is 'Table storing files processed by Spring Batch';
COMMENT ON COLUMN rgts.processed_files.id IS 'The unique ID for each file processes';
COMMENT ON COLUMN rgts.processed_files.file_name IS 'The file name processes';
COMMENT ON COLUMN rgts.processed_files.processing_status IS 'The status of the file process';
COMMENT ON COLUMN rgts.processed_files.processed_at IS 'The time the file was processed';