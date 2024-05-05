CREATE TABLE rgts.hz_set_data (
    hz_id SERIAL PRIMARY KEY,
    hz_set_name VARCHAR(255) NOT NULL,
    hz_set_value VARCHAR(255) NOT NULL
);

COMMENT ON TABLE rgts.hz_set_data IS 'Table to store hazelcast set data';
COMMENT ON COLUMN rgts.hz_set_data.hz_set_name IS 'Name of the set';
COMMENT ON COLUMN rgts.hz_set_data.hz_set_value IS 'Value stored in the set';

CREATE TABLE rgts.hz_map_data (
    hz_id SERIAL PRIMARY KEY,
    hz_map_name VARCHAR(255) NOT NULL,
    hz_map_key VARCHAR(255) UNIQUE,
    hz_map_value VARCHAR(255) NOT NULL
);

COMMENT ON TABLE rgts.hz_map_data IS 'Table to store hazelcast map data';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_name IS 'Name of the map';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_key IS 'Key in the map';
COMMENT ON COLUMN rgts.hz_map_data.hz_map_value IS 'Value associated with the key in the map';