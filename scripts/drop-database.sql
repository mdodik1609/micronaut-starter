-- PostgreSQL Database Cleanup Script
-- This script drops the database schema and tables

-- Drop trigger first
DROP TRIGGER IF EXISTS update_users_updated ON users;

-- Drop function
DROP FUNCTION IF EXISTS update_updated_column();

-- Drop indexes
DROP INDEX IF EXISTS idx_users_username;
DROP INDEX IF EXISTS idx_users_email;
DROP INDEX IF EXISTS idx_users_created;
DROP INDEX IF EXISTS idx_users_user_role;

-- Drop tables
DROP TABLE IF EXISTS users CASCADE;

-- Drop schema (optional - be careful with this)
-- DROP SCHEMA IF EXISTS public CASCADE;

-- Note: To completely drop the database, run this as superuser:
-- DROP DATABASE IF EXISTS starter; 