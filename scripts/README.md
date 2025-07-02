# Database Scripts

This folder contains SQL scripts for managing the PostgreSQL database for the Micronaut application.

## Scripts

### `init-database.sql`
Initializes the database schema with:
- Users table with all necessary columns
- Indexes for better performance
- Trigger for automatic timestamp updates
- Sample data (commented out)

### `drop-database.sql`
Cleans up the database by dropping:
- Triggers
- Functions
- Indexes
- Tables

## Usage

### Prerequisites
1. PostgreSQL installed and running
2. Database user with appropriate permissions

### Setup Database

1. **Create Database** (run as superuser):
   ```sql
   CREATE DATABASE starter;
   ```

2. **Initialize Schema**:
   ```bash
   psql -h localhost -U postgres -d starter -f scripts/init-database.sql
   ```

3. **Set Environment Variables**:
   ```bash
   export DATABASE_URL=jdbc:postgresql://localhost:5432/starter
   export DATABASE_USERNAME=postgres
   export DATABASE_PASSWORD=your_password
   ```

### Cleanup Database

```bash
psql -h localhost -U postgres -d starter -f scripts/drop-database.sql
```

### Using Docker (Alternative)

If you prefer using Docker for PostgreSQL:

```bash
# Start PostgreSQL container
docker run --name postgres-starter -e POSTGRES_PASSWORD=password -e POSTGRES_DB=starter -p 5432:5432 -d postgres:15

# Initialize database
docker exec -i postgres-starter psql -U postgres -d starter < scripts/init-database.sql
```

## Database Schema

### Users Table
- `id`: Primary key (auto-increment)
- `username`: Unique username
- `full_name`: User's full name
- `created`: Creation timestamp
- `updated`: Last update timestamp
- `email`: User's email address
- `is_validated`: Email validation status
- `password`: Hashed password
- `salt`: Password salt
- `user_role`: User role (FREE, PAID, PLUS, ADMIN)

### Indexes
- `idx_users_username`: On username column
- `idx_users_email`: On email column
- `idx_users_created`: On created timestamp
- `idx_users_user_role`: On user role

### Triggers
- `update_users_updated`: Automatically updates the `updated` timestamp on row updates 