-- Creating holdings table to store details of current user investments, non grouped
CREATE TABLE rgts.investments (
    holding_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES rgts.user_accounts(account_id) ON DELETE CASCADE NOT NULL,
    gold_type_id INT REFERENCES rgts.gold_types(gold_type_id) ON DELETE CASCADE NOT NULL,
    quantity INT CHECK (quantity > 0) DEFAULT 1,
    purchase_price NUMERIC(10,2) NOT NULL,
    purchase_date DATE DEFAULT CURRENT_DATE
);

COMMENT ON TABLE rgts.investments IS 'A table to store all current investments users';
COMMENT ON COLUMN rgts.investments.holding_id IS 'A unique identifier for each user investment';
COMMENT ON COLUMN rgts.investments.gold_type_id IS 'The ID of the gold type the user is currently investment';
COMMENT ON COLUMN rgts.investments.quantity IS 'The quantity of gold held by the user';
COMMENT ON COLUMN rgts.investments.purchase_price IS 'The price at which the user bought the gold item';
COMMENT ON COLUMN rgts.investments.purchase_date IS 'The purchase date of the gold item';

-- Creating transactions table to store details of transactions made by users
CREATE TABLE rgts.transactions (
    transaction_id SERIAL PRIMARY KEY,
    account_id INT REFERENCES rgts.user_accounts(account_id) ON DELETE CASCADE NOT NULL,
    gold_type_id INT REFERENCES rgts.gold_types(gold_type_id) ON DELETE CASCADE NOT NULL,
    quantity INT CHECK (quantity > 0) DEFAULT 1,
    transaction_type VARCHAR(4) CHECK (transaction_type IN ('BUY', 'SELL')),
    transaction_price NUMERIC(10, 2),
    transaction_date TIMESTAMP WITH TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC')
);

COMMENT ON TABLE rgts.transactions IS 'A table to store all transactions made by users';
COMMENT ON COLUMN rgts.transactions.transaction_id IS 'A unique identifier for each transaction';
COMMENT ON COLUMN rgts.transactions.account_id IS 'The ID of the account making the transaction';
COMMENT ON COLUMN rgts.transactions.gold_type_id IS 'The ID of the gold type involved in the transaction';
COMMENT ON COLUMN rgts.transactions.quantity IS 'The quantity of the gold type involved in the transaction';
COMMENT ON COLUMN rgts.transactions.transaction_type IS 'An indicator if the transaction was a buy or sell';
COMMENT ON COLUMN rgts.transactions.transaction_price IS 'The price of the transaction';
COMMENT ON COLUMN rgts.transactions.transaction_date IS 'The date and time of the transaction';



-- Creating investments table to store total holding overview for each account
CREATE TABLE rgts.holdings (
   holding_id SERIAL PRIMARY KEY,
   account_id INT REFERENCES rgts.user_accounts(account_id) ON DELETE CASCADE NOT NULL,
   total_purchase_amount NUMERIC(15, 2),
   current_value NUMERIC(15, 2),
   profit_loss NUMERIC(15, 2),
   total_weight NUMERIC(15, 2)
);

COMMENT ON TABLE rgts.holdings IS 'This table stores holdings data.';
COMMENT ON COLUMN rgts.holdings.holding_id IS 'Unique identifier for each holding';
COMMENT ON COLUMN rgts.holdings.account_id IS 'Foreign key referencing user accounts';
COMMENT ON COLUMN rgts.holdings.total_purchase_amount IS 'Total amount spent on the holding';
COMMENT ON COLUMN rgts.holdings.current_value IS 'Current value of the holding';
COMMENT ON COLUMN rgts.holdings.profit_loss IS 'Profit or loss amount';
COMMENT ON COLUMN rgts.holdings.total_weight IS 'Weight of the holding in the portfolio';

-- Indexes for investments table
CREATE INDEX idx_investments_account_id ON rgts.investments(account_id);
CREATE INDEX idx_investments_account_gold ON rgts.investments(account_id, gold_type_id);

-- Indexes for transactions table
CREATE INDEX idx_transactions_account_id ON rgts.transactions(account_id);
CREATE INDEX idx_transactions_account_date ON rgts.transactions(account_id, transaction_date);
CREATE INDEX idx_transactions_gold_type_id ON rgts.transactions(gold_type_id);

-- Index for holdings table
CREATE INDEX idx_holdings_account_id ON rgts.holdings(account_id);
