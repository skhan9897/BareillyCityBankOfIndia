-- Seed Data for BAREILLYCITYBANKOFINDIA

-- 1. Roles
INSERT INTO roles (id, name, description) VALUES
(1, 'SUPER_ADMIN', 'Super Administrator with full system control'),
(2, 'BANK_ADMIN', 'Bank Administrator with operational control'),
(3, 'BRANCH_MANAGER', 'Manager responsible for branch activities'),
(4, 'ACCOUNT_OFFICER', 'Officer in charge of accounts management'),
(5, 'CASHIER', 'Teller and cashier operational role'),
(6, 'LOAN_OFFICER', 'Officer managing loan review and approval'),
(7, 'AUDITOR', 'Read-only access for compliance and audit'),
(8, 'CUSTOMER_SUPPORT', 'Customer care representative'),
(9, 'CUSTOMER', 'Bank Account Holder');

-- 2. Branches
INSERT INTO branches (id, branch_code, branch_name, ifsc_code, address, city, state, pincode, phone, email) VALUES
(1, 'BCB001', 'Civil Lines Main Branch', 'BCBI0000001', '123 Civil Lines, Near Railway Station', 'Bareilly', 'Uttar Pradesh', '243001', '0581-2300001', 'civillines@bareillybank.com'),
(2, 'BCB002', 'Izzatnagar Branch', 'BCBI0000002', '45 Izzatnagar Industrial Area', 'Bareilly', 'Uttar Pradesh', '243122', '0581-2300002', 'izzatnagar@bareillybank.com'),
(3, 'BCB003', 'Rajendra Nagar Branch', 'BCBI0000003', '78 Rajendra Nagar Market', 'Bareilly', 'Uttar Pradesh', '243122', '0581-2300003', 'rajendranagar@bareillybank.com');

-- 3. Account Products
INSERT INTO account_products (id, product_code, product_name, account_type, min_balance, interest_rate, daily_transfer_limit, monthly_maintenance_fee) VALUES
(1, 'SAV000', 'Zero Balance Basic Savings Account (BSBD)', 'SAVINGS', 0.00, 3.25, 50000.00, 0.00),
(2, 'SAV001', 'Regular Savings Account', 'SAVINGS', 1000.00, 3.50, 100000.00, 50.00),
(3, 'SAV002', 'Premium Savings Account', 'SAVINGS', 10000.00, 4.25, 500000.00, 150.00),
(4, 'CUR001', 'Standard Current Account', 'CURRENT', 5000.00, 0.00, 1000000.00, 200.00);

-- 4. Loan Products
INSERT INTO loan_products (id, product_code, product_name, interest_rate, max_amount, max_tenure_months, processing_fee_percent) VALUES
(1, 'PL001', 'Personal Loan', 10.50, 1000000.00, 60, 1.00),
(2, 'HL001', 'Home Loan', 8.40, 10000000.00, 240, 0.50),
(3, 'EL001', 'Education Loan', 7.90, 2000000.00, 84, 0.25),
(4, 'VL001', 'Vehicle Loan', 8.90, 1500000.00, 84, 1.00);

-- 5. Double-Entry Chart of Accounts
INSERT INTO ledger_accounts (id, account_code, account_name, category, balance) VALUES
(1, '1000', 'Vault Cash & Reserve', 'ASSET', 100000000.00),
(2, '1100', 'Interbank Clearing Account', 'ASSET', 50000000.00),
(3, '1200', 'Loans Receivable', 'ASSET', 0.00),
(4, '2000', 'Customer Savings Deposits', 'LIABILITY', 0.00),
(5, '2100', 'Customer Current Deposits', 'LIABILITY', 0.00),
(6, '2200', 'Fixed Deposit Liabilities', 'LIABILITY', 0.00),
(7, '2300', 'Recurring Deposit Liabilities', 'LIABILITY', 0.00),
(8, '3000', 'Bank Capital Equity', 'EQUITY', 150000000.00),
(9, '4000', 'Interest Income on Loans', 'REVENUE', 0.00),
(10, '4100', 'Fee & Service Commission Income', 'REVENUE', 0.00),
(11, '5000', 'Interest Expense on Deposits', 'EXPENSE', 0.00);

-- 6. System Settings
INSERT INTO system_settings (setting_key, setting_value, description) VALUES
('bank_name', 'BAREILLY CITY BANK OF INDIA', 'Official Name of the Bank'),
('bank_code', 'BCBI', 'Bank Swift/System Identification Code'),
('head_office_address', 'Civil Lines, Bareilly, Uttar Pradesh - 243001', 'Headquarters Address'),
('customer_care_phone', '1800-123-4567', 'Toll-free customer care number'),
('customer_care_email', 'support@bareillybank.com', 'Customer Support Email'),
('maintenance_mode', 'false', 'Enable System Maintenance Mode'),
('max_failed_login_attempts', '5', 'Lock user account after failed login attempts'),
('lockout_duration_minutes', '15', 'Temporary lockout duration'),
('session_timeout_minutes', '30', 'Inactivity session timeout');
