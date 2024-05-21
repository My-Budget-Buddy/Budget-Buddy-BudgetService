DROP TABLE IF EXISTS monthly_summary;
DROP TABLE IF EXISTS buckets;
DROP TABLE IF EXISTS budgets;

CREATE TABLE budgets (
    budget_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(100) NOT NULL,
    total_amount INT,
	is_reserved BOOLEAN DEFAULT FALSE,
	month_year Date,
	notes VARCHAR(255),
    created_timestamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE buckets (
    bucket_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    bucket_name VARCHAR(100) NOT NULL,
    amount_required NUMERIC(10, 2) NOT NULL,
    amount_reserved NUMERIC(10, 2) NOT NULL,
    month_year Date,
	is_reserved BOOLEAN DEFAULT FALSE,
	is_active BOOLEAN DEFAULT FALSE,
    date_created TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE monthly_summary (
    summary_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    month_year DATE,
    projected_income NUMERIC(10, 2) NOT NULL,
    total_budget_amount NUMERIC(10, 2) NOT NULL
);