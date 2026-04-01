# StayHub Database Setup Guide

## Prerequisites

- PostgreSQL 12 or higher installed and running
- PostgreSQL client tools (psql)
- Database administrator privileges

---

## Quick Setup (5 minutes)

### Step 1: Create Database and User

```bash
# Connect to PostgreSQL
psql -U postgres

# Run these SQL commands
CREATE DATABASE stayhub;
CREATE USER stayhub_user WITH PASSWORD 'secure_password_here';
GRANT CONNECT ON DATABASE stayhub TO stayhub_user;
GRANT CREATE ON DATABASE stayhub TO stayhub_user;
```

### Step 2: Initialize Schema

```bash
# Run the schema migration
psql -U stayhub_user -d stayhub -f sql/001_schema.sql

# Verify tables created
psql -U stayhub_user -d stayhub -c "\dt"
```

### Step 3: Load Initial Data

```bash
# Run the data migration
psql -U stayhub_user -d stayhub -f sql/002_initial_data.sql

# Verify data loaded
psql -U stayhub_user -d stayhub -c "SELECT COUNT(*) FROM hotels;"
```

### Step 4: Configure Application

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/stayhub
    username: stayhub_user
    password: secure_password_here
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Step 5: Start Application

```bash
./mvnw spring-boot:run
```

---

## Detailed Setup

### Windows Setup

```batch
REM 1. Open Command Prompt as Administrator

REM 2. Connect to PostgreSQL
psql -U postgres -h localhost

REM 3. Execute these commands (in psql prompt)
CREATE DATABASE stayhub;
CREATE USER stayhub_user WITH PASSWORD 'secure_password_here';
GRANT CONNECT ON DATABASE stayhub TO stayhub_user;
GRANT CREATE ON DATABASE stayhub TO stayhub_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO stayhub_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO stayhub_user;

REM 4. Exit psql
\q

REM 5. Initialize schema
psql -U stayhub_user -d stayhub -h localhost -f sql\001_schema.sql

REM 6. Load initial data
psql -U stayhub_user -d stayhub -h localhost -f sql\002_initial_data.sql
```

### Linux/Mac Setup

```bash
#!/bin/bash

# 1. Connect to PostgreSQL
sudo -u postgres psql

# 2. Execute these commands (in psql prompt)
# CREATE DATABASE stayhub;
# CREATE USER stayhub_user WITH PASSWORD 'secure_password_here';
# GRANT CONNECT ON DATABASE stayhub TO stayhub_user;
# GRANT CREATE ON DATABASE stayhub TO stayhub_user;
# \q

# 3. Initialize schema
psql -U stayhub_user -d stayhub -f sql/001_schema.sql

# 4. Load initial data
psql -U stayhub_user -d stayhub -f sql/002_initial_data.sql

# 5. Verify
psql -U stayhub_user -d stayhub -c "\dt"
```

---

## Using Docker

### Option 1: Docker Container

```bash
# Start PostgreSQL container
docker run -d \
  --name stayhub-postgres \
  -e POSTGRES_USER=stayhub_user \
  -e POSTGRES_PASSWORD=secure_password_here \
  -e POSTGRES_DB=stayhub \
  -v stayhub-data:/var/lib/postgresql/data \
  -p 5432:5432 \
  postgres:15

# Wait for database to be ready (30 seconds)

# Initialize schema
docker exec -i stayhub-postgres psql -U stayhub_user -d stayhub < sql/001_schema.sql

# Load initial data
docker exec -i stayhub-postgres psql -U stayhub_user -d stayhub < sql/002_initial_data.sql
```

### Option 2: Docker Compose

Create `docker-compose.yml`:

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    container_name: stayhub-postgres
    environment:
      POSTGRES_USER: stayhub_user
      POSTGRES_PASSWORD: secure_password_here
      POSTGRES_DB: stayhub
    volumes:
      - stayhub-data:/var/lib/postgresql/data
      - ./sql:/docker-entrypoint-initdb.d
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U stayhub_user"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  stayhub-data:
```

Run with:

```bash
docker-compose up -d
# Wait for health check to pass
docker-compose logs postgres
```

---

## Verification Steps

### 1. Check Connection

```bash
psql -U stayhub_user -d stayhub -c "SELECT version();"
```

### 2. Verify Tables

```bash
psql -U stayhub_user -d stayhub -c "\dt"

# Output should show 16 tables:
# hotels, rooms, bookings, users, roles, payments, etc.
```

### 3. Check Data

```bash
# Count records in each table
psql -U stayhub_user -d stayhub << EOF
SELECT 'hotels' as table_name, COUNT(*) as count FROM hotels
UNION ALL
SELECT 'rooms', COUNT(*) FROM rooms
UNION ALL
SELECT 'room_types', COUNT(*) FROM room_types
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'roles', COUNT(*) FROM roles;
EOF
```

### 4. Test Relationships

```bash
# Check foreign keys are working
psql -U stayhub_user -d stayhub << EOF
SELECT h.name as hotel, r.room_no, rt.name as room_type
FROM rooms r
JOIN hotels h ON r.hotel_id = h.id
JOIN room_types rt ON r.room_type_id = rt.id
LIMIT 5;
EOF
```

### 5. Check Views

```bash
# List views
psql -U stayhub_user -d stayhub -c "\dv"

# Query active_bookings view
psql -U stayhub_user -d stayhub -c "SELECT * FROM active_bookings;"

# Query available_rooms view
psql -U stayhub_user -d stayhub -c "SELECT * FROM available_rooms;"

# Query hotel_statistics view
psql -U stayhub_user -d stayhub -c "SELECT * FROM hotel_statistics;"
```

---

## Troubleshooting

### Connection Issues

```bash
# Test connection
psql -U stayhub_user -h localhost -d stayhub -c "SELECT 1;"

# If fails, check:
# 1. PostgreSQL is running
sudo systemctl status postgresql  # Linux
brew services list | grep postgres  # Mac

# 2. Port 5432 is accessible
nc -zv localhost 5432

# 3. User exists
psql -U postgres -c "SELECT usename FROM pg_user WHERE usename='stayhub_user';"
```

### Schema Not Found

```bash
# Verify schema exists
psql -U stayhub_user -d stayhub -c "\dn"

# If missing, manually run:
psql -U stayhub_user -d stayhub -f sql/001_schema.sql
```

### Permission Errors

```bash
# Grant all permissions
psql -U postgres << EOF
GRANT ALL PRIVILEGES ON DATABASE stayhub TO stayhub_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO stayhub_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO stayhub_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO stayhub_user;
EOF
```

### Data Import Failed

```bash
# Check for errors
psql -U stayhub_user -d stayhub -f sql/002_initial_data.sql

# If conflicts, reset data (WARNING: Deletes all data)
psql -U stayhub_user -d stayhub << EOF
TRUNCATE TABLE notifications CASCADE;
TRUNCATE TABLE reviews CASCADE;
TRUNCATE TABLE payment_transactions CASCADE;
TRUNCATE TABLE payments CASCADE;
TRUNCATE TABLE room_amenities CASCADE;
TRUNCATE TABLE booking_rooms CASCADE;
TRUNCATE TABLE guests CASCADE;
TRUNCATE TABLE bookings CASCADE;
TRUNCATE TABLE rooms CASCADE;
TRUNCATE TABLE hotel_images CASCADE;
TRUNCATE TABLE user_roles CASCADE;
TRUNCATE TABLE amenities CASCADE;
TRUNCATE TABLE room_types CASCADE;
TRUNCATE TABLE hotels CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE roles CASCADE;
EOF

# Then re-run initial data
psql -U stayhub_user -d stayhub -f sql/002_initial_data.sql
```

---

## Development vs Production

### Development Configuration

```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/stayhub
    username: stayhub_user
    password: dev_password
  jpa:
    hibernate:
      ddl-auto: update  # Auto-update schema
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

Run with: `./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"`

### Production Configuration

```yaml
# application-production.yml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
  jpa:
    hibernate:
      ddl-auto: validate  # Never auto-update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

Run with: `java -Dspring.profiles.active=production -jar target/stayhub-0.0.1-SNAPSHOT.jar`

---

## Backup & Restore

### Backup Database

```bash
# Full backup
pg_dump -U stayhub_user -d stayhub > stayhub_backup.sql

# Compressed backup
pg_dump -U stayhub_user -d stayhub | gzip > stayhub_backup.sql.gz

# With custom format (faster restore)
pg_dump -U stayhub_user -d stayhub -Fc > stayhub_backup.dump
```

### Restore Database

```bash
# From SQL file
psql -U stayhub_user -d stayhub < stayhub_backup.sql

# From compressed file
gunzip -c stayhub_backup.sql.gz | psql -U stayhub_user -d stayhub

# From custom format
pg_restore -U stayhub_user -d stayhub stayhub_backup.dump
```

---

## Performance Tuning

### Enable Query Logging

```bash
# Connect to database
psql -U stayhub_user -d stayhub

# Enable query logging
ALTER DATABASE stayhub SET log_min_duration_statement = 0;
ALTER DATABASE stayhub SET log_statement = 'all';

# View logs (Linux)
tail -f /var/log/postgresql/postgresql.log
```

### Analyze Query Performance

```bash
# Explain query plan
EXPLAIN ANALYZE
SELECT h.name, COUNT(b.id) as booking_count
FROM hotels h
LEFT JOIN bookings b ON h.id = b.hotel_id
GROUP BY h.id, h.name
ORDER BY booking_count DESC;
```

### Optimize Indexes

```bash
# Reindex all tables
REINDEX DATABASE stayhub;

# Vacuum database
VACUUM ANALYZE;
```

---

## Migration to Production Database

### Using pg_dump and pg_restore

```bash
# 1. From dev server, create backup
pg_dump -h dev-host -U stayhub_user -d stayhub -Fc > stayhub_prod.dump

# 2. Copy to production server
scp stayhub_prod.dump user@prod-host:/tmp/

# 3. On production, restore
pg_restore -h prod-host -U stayhub_user -d stayhub < /tmp/stayhub_prod.dump
```

### Using Replication

Refer to [PostgreSQL Streaming Replication Documentation](https://www.postgresql.org/docs/current/warm-standby.html)

---

## Monitoring

### Check Database Size

```bash
psql -U stayhub_user -d stayhub << EOF
SELECT 
  schemaname,
  tablename,
  pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) as size
FROM pg_tables
WHERE schemaname NOT IN ('pg_catalog', 'information_schema')
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
EOF
```

### Monitor Active Connections

```bash
psql -U stayhub_user -d stayhub -c "SELECT * FROM pg_stat_activity;"
```

### Check Table Statistics

```bash
psql -U stayhub_user -d stayhub -c "SELECT * FROM pg_stat_user_tables;"
```

---

## Additional Resources

- [PostgreSQL Official Documentation](https://www.postgresql.org/docs/)
- [PostgreSQL Configuration](https://www.postgresql.org/docs/current/config-setting.html)
- [Query Performance Tips](https://www.postgresql.org/docs/current/sql-explain.html)

---

End of Database Setup Guide

