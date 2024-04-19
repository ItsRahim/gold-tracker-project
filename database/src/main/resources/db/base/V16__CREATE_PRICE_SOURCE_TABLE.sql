CREATE TABLE rgts.price_sources (
    source_id SERIAL PRIMARY KEY,
    source_name VARCHAR(50),
    source_endpoint VARCHAR(50),
    source_url VARCHAR(255),
    source_element_data VARCHAR(500),
    source_is_active BOOLEAN NOT NULL
);

COMMENT ON TABLE rgts.price_sources IS 'Table to store information about price sources';
COMMENT ON COLUMN rgts.price_sources.source_id IS 'Unique identifier for each source';
COMMENT ON COLUMN rgts.price_sources.source_name IS 'Name of the price source';
COMMENT ON COLUMN rgts.price_sources.source_endpoint IS 'Endpoint of the price source';
COMMENT ON COLUMN rgts.price_sources.source_url IS 'URL of the price source';
COMMENT ON COLUMN rgts.price_sources.source_element_data IS 'Data related to the HTML element for extracting prices';
COMMENT ON COLUMN rgts.price_sources.source_is_active IS 'Flag to determine if source is being used by API call';