CREATE TABLE rgts.gold_types (
    gold_type_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    net_weight NUMERIC(10, 2) NULL,
    carat VARCHAR(3) NOT NULL,
    description TEXT NOT NULL
);

COMMENT ON TABLE rgts.gold_types IS 'Table containing various gold forms';
COMMENT ON COLUMN rgts.gold_types.gold_type_id IS 'Unique identifier for different gold types';
COMMENT ON COLUMN rgts.gold_types.name IS 'The name of the gold item';
COMMENT ON COLUMN rgts.gold_types.net_weight IS 'The net weight of the gold item';
COMMENT ON COLUMN rgts.gold_types.carat IS 'The purity of the item (e.g., 18k, 24k)';
COMMENT ON COLUMN rgts.gold_types.description IS 'The description of the item';

INSERT INTO rgts.gold_types(name, net_weight, carat, description) VALUES ('XAUGBP', NULL, '24K', 'Pure Gold (24K)');

DO $$
    DECLARE
        carat INT;
    BEGIN
        FOR carat IN 1..24 LOOP
                EXECUTE 'INSERT INTO rgts.gold_types(name, net_weight, carat, description) VALUES (''' || carat || 'K'', NULL, ''' || carat || 'K'', ''' || carat || '.0 / 24'')';
            END LOOP;
    END $$;