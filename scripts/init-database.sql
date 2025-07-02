-- PostgreSQL Database Initialization Script
-- This script creates the database schema for the Micronaut application

-- Create database if it doesn't exist
-- Note: This needs to be run as a superuser or database owner
-- CREATE DATABASE starter;

-- Connect to the database
-- \c starter;

-- Create schema
CREATE SCHEMA IF NOT EXISTS public;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    email VARCHAR(255) NOT NULL,
    is_validated BOOLEAN NOT NULL DEFAULT FALSE,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    user_role VARCHAR(50) NOT NULL DEFAULT 'FREE'
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_created ON users(created);
CREATE INDEX IF NOT EXISTS idx_users_user_role ON users(user_role);

-- Create a function to update the updated timestamp
CREATE OR REPLACE FUNCTION update_updated_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update the updated timestamp
DROP TRIGGER IF EXISTS update_users_updated ON users;
CREATE TRIGGER update_users_updated
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_column();

-- Insert sample data (optional)
-- INSERT INTO users (username, full_name, created, updated, email, is_validated, password, salt, user_role)
-- VALUES 
--     ('admin', 'Administrator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin@example.com', true, 'hashedpassword', 'salt', 'ADMIN'),
--     ('user1', 'John Doe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'john@example.com', false, 'hashedpassword', 'salt', 'FREE');

-- Grant permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON DATABASE starter TO postgres;
-- GRANT ALL PRIVILEGES ON SCHEMA public TO postgres;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres; 