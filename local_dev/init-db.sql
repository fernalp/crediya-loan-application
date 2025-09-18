-- Create sequence for loan state
CREATE SEQUENCE IF NOT EXISTS loan_status_id_seq START 1 INCREMENT 1;

-- Create loan state table
CREATE TABLE IF NOT EXISTS loan_status (
    id INTEGER PRIMARY KEY DEFAULT nextval('loan_status_id_seq'),
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- Set ownership of the sequence to the loan state table
ALTER SEQUENCE loan_status_id_seq OWNED BY loan_status.id;

-- Create sequence for loan type
CREATE SEQUENCE IF NOT EXISTS loan_type_id_seq START 1 INCREMENT 1;

-- Create loan type table
CREATE TABLE IF NOT EXISTS loan_type (
    id INTEGER PRIMARY KEY DEFAULT nextval('loan_type_id_seq'),
    name VARCHAR(50) NOT NULL UNIQUE,
    minimum_amount DECIMAL(15, 2) NOT NULL,
    maximum_amount DECIMAL(15, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    automatic_approved BOOLEAN NOT NULL DEFAULT FALSE
);

-- Set ownership of the sequence to the loan type table
ALTER SEQUENCE loan_type_id_seq OWNED BY loan_type.id;

-- Create sequence for loan applications
CREATE SEQUENCE IF NOT EXISTS loan_application_id_seq START 1 INCREMENT 1;

-- Create loan application table
CREATE TABLE IF NOT EXISTS loan_application (
    id BIGINT PRIMARY KEY DEFAULT nextval('loan_application_id_seq'),
    amount DECIMAL(15,2) NOT NULL,
    term INTEGER NOT NULL,
    email VARCHAR(255) NOT NULL,
    id_loan_status INTEGER NOT NULL REFERENCES loan_status(id),
    id_loan_type INTEGER NOT NULL REFERENCES loan_type(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Set ownership of the sequence to the loan application table
ALTER SEQUENCE loan_application_id_seq OWNED BY loan_application.id;

-- Create indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_loan_status_name ON loan_status(name);
CREATE INDEX IF NOT EXISTS idx_loan_type_name ON loan_type(name);
CREATE INDEX IF NOT EXISTS idx_loan_application_email ON loan_application(email);

-- Insert default loan status

INSERT INTO loan_status (id, name, description) VALUES
    (1, 'PENDING', 'Loan application is pending approval'),
    (2, 'APPROVED', 'Loan application has been approved'),
    (3, 'REJECTED', 'Loan application has been rejected')
ON CONFLICT (name) DO NOTHING;

-- Insert default loan type
INSERT INTO loan_type (id, name, minimum_amount, maximum_amount, interest_rate, automatic_approved) VALUES
    (1, 'PERSONAL', 1000, 1000000, 5.5, false),
    (2, 'BUSINESS', 5000, 5000000, 6.5, false),
    (3, 'AUTOMATIC', 1000, 100000, 5.5, true)
ON CONFLICT (name) DO NOTHING;

-- Update the sequence to the loan status table
SELECT setval('loan_status_id_seq', (SELECT COALESCE(MAX(id), 1) FROM loan_status), true);

-- Update the sequence to the loan type table
SELECT setval('loan_type_id_seq', (SELECT COALESCE(MAX(id), 1) FROM loan_type), true);
