# StayHub Entity Mappings & Database Schema Documentation

## Table of Contents
1. [Entity Relationships](#entity-relationships)
2. [Entity Details](#entity-details)
3. [Table Specifications](#table-specifications)
4. [Indexes](#indexes)
5. [Views](#views)
6. [Constraints](#constraints)

---

## Entity Relationships

### ERD (Entity Relationship Diagram)

```
┌──────────────────────────────────────────────────────────────────────┐
│                    USER MANAGEMENT                                   │
├──────────────────────────────────────────────────────────────────────┤
│
│  Roles (1) ←──→ (N) UserRoles ←──→ (N) Users
│                                       │
│                                       ├─→ (1) Bookings (N)
│                                       ├─→ (1) Notifications (N)
│                                       └─→ (1) Reviews (N)
│
└──────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────┐
│                    HOTEL MANAGEMENT                                  │
├──────────────────────────────────────────────────────────────────────┤
│
│  Hotels (1) ─────→ (N) HotelImages
│     │
│     ├─→ (1) RoomTypes (N) ─→ (1) Rooms (N)
│     │                           │
│     │                           ├─→ (M:N) Amenities (RoomAmenities)
│     │                           └─→ (1) BookingRooms (N)
│     │
│     ├─→ (1) Bookings (N)
│     │       │
│     │       ├─→ (1) BookingRooms (N)
│     │       ├─→ (1) Guests (N)
│     │       ├─→ (1) Payments (N) ─→ (1) PaymentTransactions (N)
│     │       └─→ (1) Reviews (N)
│     │
│     └─→ (1) Reviews (N)
│
└──────────────────────────────────────────────────────────────────────┘
```

---

## Entity Details

### 1. User (Authentication & Authorization)

**JPA Entity**: `com.neo.stayhub.domain.User`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key (auto-generated) |
| fullName | VARCHAR(150) | NOT NULL | User's full name |
| email | VARCHAR(150) | NOT NULL, UNIQUE | User's email address |
| phone | VARCHAR(20) | | User's phone number |
| passwordHash | VARCHAR(255) | NOT NULL | Encrypted password |
| enabled | BOOLEAN | NOT NULL, DEFAULT: TRUE | Account enabled status |
| accountLocked | BOOLEAN | NOT NULL, DEFAULT: FALSE | Account locked flag |
| emailVerified | BOOLEAN | NOT NULL, DEFAULT: FALSE | Email verification status |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Record creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Last update timestamp |
| version | BIGINT | NOT NULL | Optimistic locking version |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Relationships**:
- `userUserRoles`: One-to-Many with UserRole (mappedBy: user)
- `userBookings`: One-to-Many with Booking (mappedBy: user)
- `userNotifications`: One-to-Many with Notification (mappedBy: user)
- `userReviews`: One-to-Many with Review (mappedBy: user)

---

### 2. Role

**JPA Entity**: `com.neo.stayhub.domain.Role`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| code | VARCHAR(30) | NOT NULL, UNIQUE | Role code (ADMIN, STAFF, GUEST) |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Role name |
| description | TEXT | | Role description |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Relationships**:
- `roleUserRoles`: One-to-Many with UserRole (mappedBy: role)

---

### 3. UserRole (Junction Table)

**JPA Entity**: `com.neo.stayhub.domain.UserRole`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| userId | BIGINT | FK, NOT NULL | Foreign key to User |
| roleId | BIGINT | FK, NOT NULL | Foreign key to Role |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- UNIQUE(userId, roleId) - Each user can have only one of each role
- FK: userId → users(id) ON DELETE CASCADE
- FK: roleId → roles(id) ON DELETE CASCADE

---

### 4. Hotel

**JPA Entity**: `com.neo.stayhub.domain.Hotel`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| code | VARCHAR(30) | NOT NULL, UNIQUE | Hotel code (e.g., PLZA) |
| name | VARCHAR(200) | NOT NULL | Hotel name |
| description | TEXT | | Hotel description |
| addressLine1 | VARCHAR(255) | NOT NULL | Primary address |
| addressLine2 | VARCHAR(255) | | Secondary address |
| city | VARCHAR(100) | NOT NULL | City name |
| state | VARCHAR(100) | | State/Province |
| country | VARCHAR(100) | NOT NULL | Country name |
| postalCode | VARCHAR(20) | | Postal code |
| latitude | NUMERIC(10,7) | | Geographic latitude |
| longitude | NUMERIC(10,7) | | Geographic longitude |
| starRating | NUMERIC(2,1) | | Hotel rating (1-5 stars) |
| status | VARCHAR(30) | NOT NULL, DEFAULT: ACTIVE | Status (ACTIVE, INACTIVE, MAINTENANCE) |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Relationships**:
- `hotelHotelImages`: One-to-Many with HotelImage (mappedBy: hotel)
- `hotelRooms`: One-to-Many with Room (mappedBy: hotel)
- `hotelBookings`: One-to-Many with Booking (mappedBy: hotel)
- `hotelReviews`: One-to-Many with Review (mappedBy: hotel)

---

### 5. HotelImage

**JPA Entity**: `com.neo.stayhub.domain.HotelImage`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| hotelId | BIGINT | FK, NOT NULL | Foreign key to Hotel |
| imageUrl | VARCHAR(500) | NOT NULL | Image URL |
| altText | VARCHAR(255) | | Alt text for accessibility |
| sortOrder | INTEGER | | Display order |
| isPrimary | BOOLEAN | NOT NULL, DEFAULT: FALSE | Primary image flag |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: hotelId → hotels(id) ON DELETE CASCADE

---

### 6. RoomType

**JPA Entity**: `com.neo.stayhub.domain.RoomType`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| code | VARCHAR(30) | NOT NULL, UNIQUE | Room type code (SINGLE, DOUBLE, SUITE) |
| name | VARCHAR(100) | NOT NULL | Room type name |
| description | TEXT | | Room type description |
| maxOccupancy | INTEGER | NOT NULL | Maximum guests |
| basePrice | NUMERIC(12,2) | NOT NULL | Base price per night |
| status | VARCHAR(30) | NOT NULL, DEFAULT: ACTIVE | Status |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Relationships**:
- `roomTypeRooms`: One-to-Many with Room (mappedBy: roomType)

---

### 7. Amenity

**JPA Entity**: `com.neo.stayhub.domain.Amenity`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| code | VARCHAR(30) | NOT NULL, UNIQUE | Amenity code (WIFI, TV, AC) |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Amenity name |
| description | TEXT | | Amenity description |
| category | VARCHAR(50) | | Category (Technology, Comfort, etc.) |
| status | VARCHAR(30) | NOT NULL, DEFAULT: ACTIVE | Status |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Relationships**:
- `amenityRooms`: Many-to-Many with Room via RoomAmenities

---

### 8. Room

**JPA Entity**: `com.neo.stayhub.domain.Room`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| hotelId | BIGINT | FK, NOT NULL | Foreign key to Hotel |
| roomTypeId | BIGINT | FK, NOT NULL | Foreign key to RoomType |
| roomNo | VARCHAR(30) | NOT NULL | Room number |
| floorNo | INTEGER | | Floor number |
| status | VARCHAR(30) | NOT NULL, DEFAULT: AVAILABLE | Status (AVAILABLE, OCCUPIED, MAINTENANCE) |
| smokingAllowed | BOOLEAN | NOT NULL, DEFAULT: FALSE | Smoking allowed flag |
| priceOverride | NUMERIC(12,2) | | Override price (if different from RoomType) |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- UNIQUE(hotelId, roomNo) - Room number must be unique per hotel
- FK: hotelId → hotels(id) ON DELETE CASCADE
- FK: roomTypeId → room_types(id)

**Relationships**:
- `roomHotel`: Many-to-One with Hotel (JoinColumn: hotel_id)
- `roomRoomType`: Many-to-One with RoomType (JoinColumn: room_type_id)
- `roomAmenityAmenities`: Many-to-Many with Amenity via RoomAmenities
- `roomBookingRooms`: One-to-Many with BookingRoom (mappedBy: room)

---

### 9. RoomAmenities (Junction Table)

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| roomId | BIGINT | FK, PK NOT NULL | Foreign key to Room |
| amenityId | BIGINT | FK, PK NOT NULL | Foreign key to Amenity |

**Constraints**:
- PK: (roomId, amenityId)
- FK: roomId → rooms(id) ON DELETE CASCADE
- FK: amenityId → amenities(id) ON DELETE CASCADE

---

### 10. Booking

**JPA Entity**: `com.neo.stayhub.domain.Booking`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| userId | BIGINT | FK, NOT NULL | Foreign key to User |
| hotelId | BIGINT | FK, NOT NULL | Foreign key to Hotel |
| bookingNo | VARCHAR(40) | NOT NULL, UNIQUE | Unique booking number |
| checkIn | DATE | NOT NULL | Check-in date |
| checkOut | DATE | NOT NULL | Check-out date |
| adults | INTEGER | NOT NULL | Number of adults |
| children | INTEGER | NOT NULL | Number of children |
| status | VARCHAR(30) | NOT NULL, DEFAULT: PENDING | Status (PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED) |
| currency | VARCHAR(10) | NOT NULL, DEFAULT: USD | Currency code |
| subtotal | NUMERIC(12,2) | NOT NULL | Subtotal amount |
| taxAmount | NUMERIC(12,2) | NOT NULL | Tax amount |
| discountAmount | NUMERIC(12,2) | NOT NULL, DEFAULT: 0 | Discount amount |
| totalAmount | NUMERIC(12,2) | NOT NULL | Total amount |
| specialRequests | TEXT | | Special requests from guest |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: userId → users(id) ON DELETE RESTRICT
- FK: hotelId → hotels(id) ON DELETE RESTRICT

**Relationships**:
- `bookingUser`: Many-to-One with User (JoinColumn: user_id)
- `bookingHotel`: Many-to-One with Hotel (JoinColumn: hotel_id)
- `bookingBookingRooms`: One-to-Many with BookingRoom (mappedBy: booking)
- `bookingGuests`: One-to-Many with Guest (mappedBy: booking)
- `bookingPayments`: One-to-Many with Payment (mappedBy: booking)
- `bookingReviews`: One-to-Many with Review (mappedBy: booking)

---

### 11. BookingRoom (Junction Table)

**JPA Entity**: `com.neo.stayhub.domain.BookingRoom`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| bookingId | BIGINT | FK, NOT NULL | Foreign key to Booking |
| roomId | BIGINT | FK, NOT NULL | Foreign key to Room |
| quantity | INTEGER | NOT NULL, DEFAULT: 1 | Number of this room type |
| pricePerNight | NUMERIC(12,2) | NOT NULL | Price per night |
| numberOfNights | INTEGER | NOT NULL | Number of nights |
| totalPrice | NUMERIC(12,2) | NOT NULL | Total price for this room |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: bookingId → bookings(id) ON DELETE CASCADE
- FK: roomId → rooms(id) ON DELETE RESTRICT

---

### 12. Guest

**JPA Entity**: `com.neo.stayhub.domain.Guest`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| bookingId | BIGINT | FK, NOT NULL | Foreign key to Booking |
| guestName | VARCHAR(150) | NOT NULL | Guest's full name |
| guestEmail | VARCHAR(150) | | Guest's email address |
| guestPhone | VARCHAR(20) | | Guest's phone number |
| guestNationality | VARCHAR(100) | | Guest's nationality |
| guestDocumentNumber | VARCHAR(50) | | ID document number |
| isPrimaryGuest | BOOLEAN | NOT NULL, DEFAULT: FALSE | Primary guest flag |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: bookingId → bookings(id) ON DELETE CASCADE

---

### 13. Payment

**JPA Entity**: `com.neo.stayhub.domain.Payment`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| bookingId | BIGINT | FK, NOT NULL | Foreign key to Booking |
| gateway | VARCHAR(50) | NOT NULL | Payment gateway (Stripe, PayPal, etc.) |
| gatewayOrderId | VARCHAR(100) | | Order ID from payment gateway |
| gatewayPaymentId | VARCHAR(100) | | Payment ID from payment gateway |
| paymentReference | VARCHAR(100) | | Internal payment reference |
| amount | NUMERIC(12,2) | NOT NULL | Payment amount |
| currency | VARCHAR(10) | NOT NULL, DEFAULT: USD | Currency code |
| status | VARCHAR(30) | NOT NULL, DEFAULT: PENDING | Status (PENDING, COMPLETED, FAILED, REFUNDED) |
| paidAt | TIMESTAMP WITH TIME ZONE | | Payment completion timestamp |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: bookingId → bookings(id) ON DELETE RESTRICT

**Relationships**:
- `paymentBooking`: Many-to-One with Booking (JoinColumn: booking_id)
- `paymentPaymentTransactions`: One-to-Many with PaymentTransaction (mappedBy: payment)

---

### 14. PaymentTransaction

**JPA Entity**: `com.neo.stayhub.domain.PaymentTransaction`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| paymentId | BIGINT | FK, NOT NULL | Foreign key to Payment |
| transactionType | VARCHAR(30) | NOT NULL | Type (CHARGE, REFUND, ADJUSTMENT) |
| amount | NUMERIC(12,2) | NOT NULL | Transaction amount |
| status | VARCHAR(30) | NOT NULL, DEFAULT: PENDING | Status |
| gatewayTransactionId | VARCHAR(100) | | ID from payment gateway |
| responseCode | VARCHAR(50) | | Response code from gateway |
| responseMessage | TEXT | | Response message from gateway |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: paymentId → payments(id) ON DELETE CASCADE

---

### 15. Review

**JPA Entity**: `com.neo.stayhub.domain.Review`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| bookingId | BIGINT | FK, NOT NULL | Foreign key to Booking |
| hotelId | BIGINT | FK, NOT NULL | Foreign key to Hotel |
| userId | BIGINT | FK, NOT NULL | Foreign key to User |
| rating | INTEGER | NOT NULL, CHECK: 1-5 | Rating (1-5 stars) |
| comment | TEXT | | Review comment |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- CHECK: rating >= 1 AND rating <= 5
- FK: bookingId → bookings(id) ON DELETE CASCADE
- FK: hotelId → hotels(id) ON DELETE CASCADE
- FK: userId → users(id) ON DELETE RESTRICT

**Relationships**:
- `reviewBooking`: Many-to-One with Booking (JoinColumn: booking_id)
- `reviewHotel`: Many-to-One with Hotel (JoinColumn: hotel_id)
- `reviewUser`: Many-to-One with User (JoinColumn: user_id)

---

### 16. Notification

**JPA Entity**: `com.neo.stayhub.domain.Notification`

| Field | Type | Constraints | Description |
|-------|------|-------------|-------------|
| id | BIGINT | PK, NOT NULL | Primary key |
| userId | BIGINT | FK, NOT NULL | Foreign key to User |
| title | VARCHAR(255) | NOT NULL | Notification title |
| message | TEXT | NOT NULL | Notification message |
| notificationType | VARCHAR(50) | | Type (BOOKING, PAYMENT, REVIEW, etc.) |
| relatedEntityId | BIGINT | | ID of related entity |
| relatedEntityType | VARCHAR(50) | | Type of related entity |
| isRead | BOOLEAN | NOT NULL, DEFAULT: FALSE | Read status |
| readAt | TIMESTAMP WITH TIME ZONE | | Timestamp when read |
| createdAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Creation timestamp |
| updatedAt | TIMESTAMP WITH TIME ZONE | NOT NULL | Update timestamp |
| version | BIGINT | NOT NULL | Version for optimistic locking |
| dateCreated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit creation timestamp |
| lastUpdated | TIMESTAMP WITH TIME ZONE | NOT NULL | Audit update timestamp |

**Constraints**:
- FK: userId → users(id) ON DELETE CASCADE

**Relationships**:
- `notificationUser`: Many-to-One with User (JoinColumn: user_id)

---

## Table Specifications

### Sequence
- **Name**: primary_sequence
- **Start**: 10000
- **Increment**: 1

### Data Types
- **ID Fields**: BIGINT with SEQUENCE
- **Timestamps**: TIMESTAMP WITH TIME ZONE
- **Decimals**: NUMERIC(precision, scale)
- **Text Fields**: VARCHAR for limited, TEXT for unlimited

### Audit Fields (All Tables)
- `dateCreated`: Auto-set on creation (JPA @CreatedDate)
- `lastUpdated`: Auto-updated on modification (JPA @LastModifiedDate)

---

## Indexes

### Primary Indexes (All tables have by default)
- PK: id (primary_sequence)

### Foreign Key Indexes
Created automatically for foreign key columns

### Custom Performance Indexes

```sql
-- Users
CREATE INDEX idx_users_email ON users(email);

-- Roles
CREATE INDEX idx_roles_code ON roles(code);

-- Hotels
CREATE INDEX idx_hotels_code ON hotels(code);
CREATE INDEX idx_hotels_city ON hotels(city);
CREATE INDEX idx_hotels_status ON hotels(status);

-- Rooms
CREATE INDEX idx_rooms_hotel_id ON rooms(hotel_id);
CREATE INDEX idx_rooms_room_type_id ON rooms(room_type_id);
CREATE INDEX idx_rooms_status ON rooms(status);

-- Bookings
CREATE INDEX idx_bookings_user_id ON bookings(user_id);
CREATE INDEX idx_bookings_hotel_id ON bookings(hotel_id);
CREATE INDEX idx_bookings_booking_no ON bookings(booking_no);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_bookings_check_in ON bookings(check_in);
CREATE INDEX idx_bookings_check_out ON bookings(check_out);

-- Payments
CREATE INDEX idx_payments_booking_id ON payments(booking_id);
CREATE INDEX idx_payments_gateway_payment_id ON payments(gateway_payment_id);
CREATE INDEX idx_payments_status ON payments(status);

-- Reviews
CREATE INDEX idx_reviews_booking_id ON reviews(booking_id);
CREATE INDEX idx_reviews_hotel_id ON reviews(hotel_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_reviews_rating ON reviews(rating);

-- Notifications
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);
```

---

## Views

### 1. active_bookings
Shows currently active bookings with guest and hotel information

```sql
SELECT id, booking_no, check_in, check_out, total_amount,
       guest_name, hotel_name, status
FROM active_bookings
ORDER BY check_in DESC;
```

### 2. available_rooms
Shows all available rooms with pricing

```sql
SELECT id, room_no, hotel_name, room_type, price, city
FROM available_rooms
ORDER BY hotel_name, room_no;
```

### 3. hotel_statistics
Shows hotel performance metrics

```sql
SELECT id, name, city, total_rooms, total_bookings, avg_rating
FROM hotel_statistics
ORDER BY avg_rating DESC;
```

---

## Constraints

### Unique Constraints
- `users.email` - UNIQUE
- `roles.code` - UNIQUE
- `roles.name` - UNIQUE
- `hotels.code` - UNIQUE
- `room_types.code` - UNIQUE
- `amenities.code` - UNIQUE
- `amenities.name` - UNIQUE
- `rooms (hotel_id, room_no)` - UNIQUE
- `bookings.booking_no` - UNIQUE
- `user_roles (user_id, role_id)` - UNIQUE

### Check Constraints
- `reviews.rating` - CHECK (rating >= 1 AND rating <= 5)

### Foreign Key Constraints
All documented in entity specifications above

---

## Key Design Patterns

### 1. Soft Delete Pattern
Not implemented - uses CASCADE delete with event listeners for cleanup

### 2. Audit Trail
All tables include:
- `dateCreated`: Creation timestamp
- `lastUpdated`: Last modification timestamp
- `version`: For optimistic locking

### 3. Optimistic Locking
- `version` field on all entities
- Prevents concurrent update conflicts

### 4. Sequence Generation
- Single `primary_sequence` for all entities
- Starts at 10000 for easy identification of domain-generated IDs

### 5. Soft Relationships
- `hotelId` and `userId` in bookings use RESTRICT delete
- Prevents accidental data loss
- Maintains referential integrity

---

## Migration Strategy

### Development
```sql
-- Use this for development/testing
ALTER TABLE bookings DROP CONSTRAINT fk_bookings_user;
ALTER TABLE bookings ADD CONSTRAINT fk_bookings_user 
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
```

### Production
```sql
-- Use CASCADE only after archiving old data
-- Default is RESTRICT to prevent accidental deletions
```

---

## Performance Considerations

1. **Indexes**: Key lookups optimized for common queries
2. **Foreign Keys**: Appropriate ON DELETE actions to maintain integrity
3. **Sequences**: Single sequence for simplicity, consider sharding for ultra-high volume
4. **Partitioning**: Consider partitioning `bookings` and `payments` by date for large datasets
5. **Caching**: Hotel, RoomType, Amenity data can be cached

---

End of Entity Mappings Documentation

