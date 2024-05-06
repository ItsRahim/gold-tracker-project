CREATE TABLE rgts.hz_set_data (
    hz_set_id SERIAL PRIMARY KEY,
    hz_set_name VARCHAR(255) NOT NULL,
    hz_set_value VARCHAR(255) NOT NULL,
    hz_set_created TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC')
);

COMMENT ON TABLE rgts.hz_set_data IS 'Table to store hazelcast set data';
COMMENT ON COLUMN rgts.hz_set_data.hz_set_name IS 'Name of the set';
COMMENT ON COLUMN rgts.hz_set_data.hz_set_value IS 'Value stored in the set';
COMMENT ON COLUMN rgts.hz_set_data.hz_set_created IS 'The time the set value was created';

CREATE TABLE rgts.hz_map_data (
    hz_map_id SERIAL PRIMARY KEY,
    hz_map_name VARCHAR(255) NOT NULL,
    hz_map_key VARCHAR(255) UNIQUE,
    hz_map_value VARCHAR(255) NOT NULL,
    hz_map_updated_at TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC')
);

CREATE OR REPLACE FUNCTION update_hz_map_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.hz_map_updated_at := NOW() AT TIME ZONE 'UTC';
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER hz_map_data_update_trigger
BEFORE UPDATE OF hz_map_value ON rgts.hz_map_data
FOR EACH ROW
EXECUTE FUNCTION update_hz_map_updated_at();

COMMENT ON TABLE rgts.hz_map_data IS 'Table to store hazelcast map data';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_name IS 'Name of the map';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_key IS 'Key in the map';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_value IS 'Value associated with the key in the map';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_updated_at IS 'The time the map key was last updated';