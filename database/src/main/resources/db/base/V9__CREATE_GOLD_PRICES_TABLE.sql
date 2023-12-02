CREATE TABLE rgts.gold_prices (
    price_id SERIAL PRIMARY KEY,
    gold_type_id INT REFERENCES rgts.gold_types(gold_type_id),
    current_price NUMERIC(8,2) NOT NULL,
    updated_at TIMESTAMPTZ(0) DEFAULT NOW()
);

COMMENT ON TABLE rgts.gold_prices IS 'Table storing historical gold prices for different gold types';
COMMENT ON COLUMN rgts.gold_prices.price_id IS 'Unique identifier for each gold price entry';
COMMENT ON COLUMN rgts.gold_prices.gold_type_id IS 'Foreign key referencing the gold type associated with the price';
COMMENT ON COLUMN rgts.gold_prices.current_price IS 'The gold price for a specific gold type (in the specified currency)';
COMMENT ON COLUMN rgts.gold_prices.updated_at IS 'Timestamp indicating when the gold price entry was last updated';

INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (1, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (2, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (3, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (4, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (5, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (6, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (7, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (8, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (9, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (10, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (11, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (12, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (13, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (14, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (15, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (16, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (17, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (18, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (19, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (20, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (21, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (22, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (23, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (24, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (25, 0);
INSERT INTO rgts.gold_prices(gold_type_id, current_price) VALUES (26, 0);