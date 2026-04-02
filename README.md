# StayHub - Hotel Booking Management System

A comprehensive Spring Boot application for managing hotel bookings, reservations, and guest interactions. Built with modern technologies and best practices for scalability and maintainability.

## 🏨 Project Overview

StayHub is a fully-featured hotel management platform that enables:
- Hotel management and room inventory
- Guest bookings and reservations
- Payment processing and transaction tracking
- Guest reviews and feedback
- Multi-user role-based access control
- Notification system for guests and administrators

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.4.4
- **Language**: Java 17
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)

### Libraries & Dependencies
- **Lombok**: For reducing boilerplate code with annotations
- **Spring Validation**: For input validation (JSR-380)
- **Spring Web**: RESTful web services
- **PostgreSQL Driver**: Database connectivity

### Development Tools
- **IDE**: IntelliJ IDEA or any Java-compatible IDE
- **Version Control**: Git
- **Maven Wrapper**: For consistent builds across environments

## 📋 Project Structure

```
stayhub/
├── src/
│   ├── main/
│   │   ├── java/com/neo/stayhub/
│   │   │   ├── config/          # Spring configuration classes
│   │   │   ├── domain/          # JPA entity models
│   │   │   ├── events/          # Domain event listeners for cascade operations
│   │   │   ├── model/           # DTOs (Data Transfer Objects)
│   │   │   ├── repos/           # Spring Data JPA repositories
│   │   │   ├── rest/            # REST API controllers
│   │   │   ├── service/         # Business logic services
│   │   │   ├── util/            # Utility classes and exceptions
│   │   │   └── StayhubApplication.java
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/                     # Unit and integration tests (if any)
├── pom.xml                      # Maven project configuration
├── mvnw / mvnw.cmd             # Maven wrapper scripts
├── README.md                    # Project overview
└── .gitignore                   # Git ignore patterns

```

## 🗄️ Database Architecture

### Entity Relationships

```
Users
├── UserRoles (1:N)
├── Bookings (1:N)
├── Notifications (1:N)
└── Reviews (1:N)

Hotels
├── HotelImages (1:N)
├── Rooms (1:N)
├── Bookings (1:N)
└── Reviews (1:N)

Rooms
├── RoomType (N:1)
├── Hotel (N:1)
├── Amenities (N:M via RoomAmenities)
└── BookingRooms (1:N)

Bookings
├── User (N:1)
├── Hotel (N:1)
├── BookingRooms (1:N)
├── Guests (1:N)
├── Payments (1:N)
└── Reviews (1:N)

Payments
├── Booking (N:1)
└── PaymentTransactions (1:N)

Reviews
├── Booking (N:1)
├── Hotel (N:1)
└── User (N:1)
```

## 🔐 Core Entities

### Users & Roles
- **User**: Represents system users (guests, staff, admins)
- **Role**: Defines user roles (Admin, Staff, Guest)
- **UserRole**: Junction table for user-role relationships

### Hotel Management
- **Hotel**: Hotel property information and details
- **HotelImage**: Hotel images and gallery
- **RoomType**: Types of rooms (Single, Double, Suite, etc.)
- **Room**: Individual room units with amenities
- **Amenity**: Room amenities (WiFi, TV, AC, etc.)

### Booking System
- **Booking**: Main booking records
- **BookingRoom**: Details about which rooms are booked
- **Guest**: Guest information linked to bookings
- **Payment**: Payment records for bookings
- **PaymentTransaction**: Individual transaction details
- **Review**: Guest reviews and ratings

### Notifications
- **Notification**: System notifications for users

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+ or use Maven wrapper
- PostgreSQL 12 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Sreeja-Shaganti/stayhub.git
   cd stayhub
   ```

2. **Configure Database**
   
   Create a PostgreSQL database:
   ```sql
   CREATE DATABASE stayhub;
   CREATE USER stayhub_user WITH PASSWORD 'your_password';
   GRANT ALL PRIVILEGES ON DATABASE stayhub TO stayhub_user;
   ```

3. **Update Application Configuration**
   
   Edit `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/stayhub
       username: stayhub_user
       password: your_password
       driver-class-name: org.postgresql.Driver
     jpa:
       hibernate:
         ddl-auto: validate  # Use 'update' for development
       properties:
         hibernate:
           dialect: org.hibernate.dialect.PostgreSQLDialect
   ```

4. **Initialize Database Schema**
   
   Run the SQL migration files from `sql/` directory:
   ```bash
   psql -U stayhub_user -d stayhub -f sql/001_schema.sql
   psql -U stayhub_user -d stayhub -f sql/002_initial_data.sql
   ```

5. **Build the Application**
   ```bash
   ./mvnw clean package
   ```

6. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or using Java:
   ```bash
   java -jar target/stayhub-0.0.1-SNAPSHOT.jar
   ```

   The application will be accessible at: `http://localhost:8080`

## 📚 API Endpoints

### User Management
- `GET /api/users` - List all users
- `POST /api/users` - Create new user
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Hotel Management
- `GET /api/hotels` - List all hotels
- `POST /api/hotels` - Create new hotel
- `GET /api/hotels/{id}` - Get hotel details
- `PUT /api/hotels/{id}` - Update hotel
- `DELETE /api/hotels/{id}` - Delete hotel
- `GET /api/hotels/{id}/rooms` - Get rooms for hotel
- `GET /api/hotels/{id}/reviews` - Get reviews for hotel

### Room Management
- `GET /api/rooms` - List all rooms
- `POST /api/rooms` - Create new room
- `GET /api/rooms/{id}` - Get room details
- `PUT /api/rooms/{id}` - Update room
- `DELETE /api/rooms/{id}` - Delete room

### Booking Management
- `GET /api/bookings` - List all bookings
- `POST /api/bookings` - Create new booking
- `GET /api/bookings/{id}` - Get booking details
- `PUT /api/bookings/{id}` - Update booking
- `DELETE /api/bookings/{id}` - Cancel booking

### Payments
- `GET /api/payments` - List all payments
- `POST /api/payments` - Create payment
- `GET /api/payments/{id}` - Get payment details
- `POST /api/payments/{id}/transactions` - Record transaction

### Reviews
- `GET /api/reviews` - List all reviews
- `POST /api/reviews` - Create review
- `GET /api/reviews/{id}` - Get review details
- `PUT /api/reviews/{id}` - Update review
- `DELETE /api/reviews/{id}` - Delete review

## 🔧 Configuration

### Application Profiles

The application supports multiple profiles for different environments:

```bash
# Development with local database
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"

# Production deployment
java -Dspring.profiles.active=production -jar target/stayhub-0.0.1-SNAPSHOT.jar
```

### Environment Variables

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Hibernate DDL strategy (validate, update, create, create-drop)

## 💾 Database Schema

### Key Tables

1. **Users**: User accounts and authentication
2. **Roles**: User role definitions
3. **UserRoles**: User-to-role mappings
4. **Hotels**: Hotel property information
5. **HotelImages**: Hotel images
6. **RoomTypes**: Room type definitions
7. **Rooms**: Individual room units
8. **Amenities**: Room amenities catalog
9. **RoomAmenities**: Room-to-amenity mappings
10. **Bookings**: Booking records
11. **BookingRooms**: Booked room details
12. **Guests**: Guest information
13. **Payments**: Payment records
14. **PaymentTransactions**: Transaction details
15. **Reviews**: Guest reviews
16. **Notifications**: System notifications

For detailed schema definition, see: [Database Schema](sql/001_schema.sql)

## 🏗️ Architecture Overview

### Layered Architecture

```
┌─────────────────────────────────────┐
│   REST Controller Layer             │  (rest/)
│   - HTTP endpoints                  │
│   - Request/Response handling       │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Service Layer                     │  (service/)
│   - Business logic                  │
│   - Transaction management          │
│   - Data validation                 │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Repository Layer                  │  (repos/)
│   - Data access operations          │
│   - Database queries                │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│   Database Layer                    │
│   - PostgreSQL Database             │
└─────────────────────────────────────┘
```

## 🔄 Data Transfer Objects (DTOs)

DTOs are used to transfer data between layers:
- **Request DTOs**: Receive data from clients
- **Response DTOs**: Send data to clients
- **Mapping**: Automatic conversion between domain models and DTOs

Located in: `src/main/java/com/neo/stayhub/model/`

## 🛡️ Exception Handling

- **NotFoundException**: Resource not found (404)
- **ReferencedException**: Referenced entity still in use (409)
- **ValidationException**: Input validation errors (400)
- **UnauthorizedException**: Authentication/Authorization issues (401/403)

## 🚢 Deployment

### Building Docker Image
```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=sreeja/stayhub
```

### Running Docker Container
```bash
docker run -e SPRING_PROFILES_ACTIVE=production \
           -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/stayhub \
           -e SPRING_DATASOURCE_USERNAME=stayhub_user \
           -e SPRING_DATASOURCE_PASSWORD=your_password \
           -p 8080:8080 \
           sreeja/stayhub
```

## 📖 Additional Resources

- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Lombok Documentation](https://projectlombok.org/)

## 📧 Contact & Support

- **Author**: Sreeja Shaganti
- **Repository**: https://github.com/Sreeja-Shaganti/stayhub
- **Email**: sreeja.shaganti@github.com

## 📄 License

This project is licensed under the MIT License.

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'feat: Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 Changelog

### Version 0.0.1 (Initial Release)
- Initial project setup with Spring Boot 3.4.4
- Complete domain model implementation
- REST API controllers for all entities
- Service layer with business logic
- Database repository layer
- Configuration and utility classes

