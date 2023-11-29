CREATE TABLE rgts.gold_prices (
    price_id SERIAL PRIMARY KEY,
    gold_type_id INT REFERENCES rgts.gold_types(gold_type_id),
    price NUMERIC(5,2) NOT NULL,
    updated_at TIMESTAMPTZ(0)
);

COMMENT ON TABLE rgts.gold_prices IS 'Table storing historical gold prices for different gold types';
COMMENT ON COLUMN rgts.gold_prices.price_id IS 'Unique identifier for each gold price entry';
COMMENT ON COLUMN rgts.gold_prices.gold_type_id IS 'Foreign key referencing the gold type associated with the price';
COMMENT ON COLUMN rgts.gold_prices.price IS 'The gold price for a specific gold type (in the specified currency)';
COMMENT ON COLUMN rgts.gold_prices.updated_at IS 'Timestamp indicating when the gold price entry was last updated';