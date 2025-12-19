-- PostgreSQL setup script for Leave Management System

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    userId SERIAL PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(50),
    leaveBalance INT
);

-- Create leave_requests table
CREATE TABLE IF NOT EXISTS leave_requests (
    leaveRequestId SERIAL PRIMARY KEY,
    userId INT,
    leaveStatus VARCHAR(50),
    leaveDays INT,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Insert sample data
INSERT INTO users (userId, name, role, leaveBalance) VALUES
(101, 'John Employee', 'Employee', 10),
(102, 'Jane Manager', 'Manager', 15)
ON CONFLICT (userId) DO NOTHING;

-- Create role 'kavi' if it doesn't exist (set password to 'kavi')
DO $$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'kavi') THEN
      CREATE ROLE kavi LOGIN PASSWORD 'kavi';
   END IF;
END
$$;

-- Grant privileges (assuming schema is public)
GRANT ALL PRIVILEGES ON SCHEMA public TO kavi;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kavi;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kavi;

SELECT 'Database setup complete!' AS Status;
