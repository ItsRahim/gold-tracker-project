CREATE TABLE gold_price_history (
    price_id SERIAL PRIMARY KEY,
    price_ounce NUMERIC(10, 2) NOT NULL,
    price_gram NUMERIC(10, 2) NOT NULL,
    effective_date DATE
);

COMMENT ON TABLE gold_price_history IS 'Table storing historical records of daily gold prices';
COMMENT ON COLUMN gold_price_history.price_id IS 'Unique identifier for each daily gold price entry';
COMMENT ON COLUMN gold_price_history.price_ounce IS 'The closing gold price per troy ounce on a specific day (in the specified currency)';
COMMENT ON COLUMN gold_price_history.price_gram IS 'The closing gold price per gram on a specific day (in the specified currency)';
COMMENT ON COLUMN gold_price_history.effective_date IS 'Date indicating when the gold prices are effective';