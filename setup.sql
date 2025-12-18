-- MySQL setup script for Leave Management System

-- Create database
CREATE DATABASE IF NOT EXISTS leave_management;
USE leave_management;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    userId INT PRIMARY KEY,
    name VARCHAR(100),
    role VARCHAR(50),
    leaveBalance INT
);

-- Create leave_requests table
CREATE TABLE IF NOT EXISTS leave_requests (
    leaveRequestId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT,
    leaveStatus VARCHAR(50),
    leaveDays INT,
    FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Insert sample data
INSERT IGNORE INTO users VALUES 
(101, 'John Employee', 'Employee', 10),
(102, 'Jane Manager', 'Manager', 15);

-- Create user 'kavi' if it doesn't exist (set password to 'kavi')
CREATE USER IF NOT EXISTS 'kavi'@'localhost' IDENTIFIED BY 'kavi';
GRANT ALL PRIVILEGES ON leave_management.* TO 'kavi'@'localhost';
FLUSH PRIVILEGES;

SELECT 'Database setup complete!' AS Status;
