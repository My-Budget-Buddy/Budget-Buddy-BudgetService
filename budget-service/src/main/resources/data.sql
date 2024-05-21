INSERT INTO budgets (user_id, category, spent_amount, is_reserved, month_year, notes) VALUES
(1, 'Groceries', 150.00, FALSE, '2024-05-01', 'Weekly grocery shopping'),
(1, 'Rent', 1200.00, TRUE, '2024-05-01', 'May rent payment'),
(2, 'Utilities', 200.00, FALSE, '2024-05-01', 'Electricity and water bill'),
(2, 'Internet', 50.00, TRUE, '2024-05-01', 'Monthly internet bill'),
(3, 'Entertainment', 100.00, FALSE, '2024-05-01', 'Movies and dining out'),
(3, 'Savings', 500.00, TRUE, '2024-05-01', 'Monthly savings deposit');

INSERT INTO buckets (user_id, bucket_name, amount_required, amount_available, month_year, is_reserved, is_active) VALUES
(1, 'Vacation Fund', 2000.00, 500.00, '2024-05-01', FALSE, TRUE),
(1, 'Emergency Fund', 1000.00, 300.00, '2024-05-01', TRUE, TRUE),
(2, 'Car Maintenance', 500.00, 100.00, '2024-05-01', FALSE, FALSE),
(2, 'Home Improvement', 1500.00, 750.00, '2024-05-01', TRUE, TRUE),
(3, 'New Laptop', 1200.00, 600.00, '2024-05-01', FALSE, TRUE),
(3, 'Health Insurance', 800.00, 400.00, '2024-05-01', TRUE, FALSE);

INSERT INTO monthly_summary (user_id, month_year, projected_income, total_budget_amount) VALUES
(1, '2024-05-01', 7777.00, 1111.00),
(2, '2024-05-01', 9999.00, 3333.00),
(3, '2024-05-01', 9876.00, 999.00);
