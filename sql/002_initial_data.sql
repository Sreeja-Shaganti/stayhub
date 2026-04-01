-- ============================================================================
-- StayHub Initial Data
-- PostgreSQL SQL Data Migration Script
-- Version: 1.0
-- Description: Initial seed data for StayHub application
-- ============================================================================

-- ============================================================================
-- ROLES DATA
-- ============================================================================

INSERT INTO roles (id, code, name, description, created_at, updated_at, version, date_created, last_updated)
VALUES
    (nextval('primary_sequence'), 'ADMIN', 'Administrator', 'Full system access', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'STAFF', 'Staff Member', 'Hotel staff access', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'GUEST', 'Guest', 'Guest account access', NOW(), NOW(), 1, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ============================================================================
-- ROOM TYPES DATA
-- ============================================================================

INSERT INTO room_types (id, code, name, description, max_occupancy, base_price, status, created_at, updated_at, version, date_created, last_updated)
VALUES
    (nextval('primary_sequence'), 'SINGLE', 'Single Room', 'Single bed room with bathroom', 1, 50.00, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'DOUBLE', 'Double Room', 'Double bed room with modern amenities', 2, 80.00, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'TWIN', 'Twin Room', 'Two single beds room', 2, 85.00, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'SUITE', 'Suite', 'Luxury suite with living area', 4, 150.00, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'DELUXE', 'Deluxe Room', 'Spacious room with premium amenities', 3, 120.00, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ============================================================================
-- AMENITIES DATA
-- ============================================================================

INSERT INTO amenities (id, code, name, description, category, status, created_at, updated_at, version, date_created, last_updated)
VALUES
    (nextval('primary_sequence'), 'WIFI', 'WiFi', 'Free wireless internet', 'Technology', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'TV', 'Television', 'Flat screen TV with cable channels', 'Entertainment', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'AC', 'Air Conditioning', 'Climate controlled room', 'Comfort', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'MINI_BAR', 'Mini Bar', 'In-room refrigerated mini bar', 'Food & Beverage', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'SAFE', 'Safety Box', 'In-room safety deposit box', 'Security', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'PHONE', 'Telephone', 'In-room landline telephone', 'Communication', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'HAIRDRYER', 'Hair Dryer', 'In-room hair dryer', 'Bathroom', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'BATHROBE', 'Bathrobe', 'Complimentary bathrobe', 'Bathroom', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'DESK', 'Work Desk', 'Spacious work desk', 'Business', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'KETTLE', 'Tea/Coffee Kettle', 'In-room tea and coffee making facilities', 'Food & Beverage', 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ============================================================================
-- SAMPLE ADMIN USER
-- ============================================================================

INSERT INTO users (id, full_name, email, phone, password_hash, enabled, account_locked, email_verified, created_at, updated_at, version, date_created, last_updated)
VALUES
    (nextval('primary_sequence'), 'Admin User', 'admin@stayhub.com', '+1-555-0100',
     '$2a$10$slYQmyNdGzin7olVMyCueuK7lgoZUJLY4Jjl8h2a5P6hSE0Fd1Cly', -- password: admin123
     TRUE, FALSE, TRUE, NOW(), NOW(), 1, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ============================================================================
-- SAMPLE HOTELS
-- ============================================================================

INSERT INTO hotels (id, code, name, description, address_line1, address_line2, city, state, country, postal_code, latitude, longitude, star_rating, status, created_at, updated_at, version, date_created, last_updated)
VALUES
    (nextval('primary_sequence'), 'PLZA', 'Plaza Hotel Downtown', 'Modern luxury hotel in the heart of downtown',
     '100 Main Street', 'Downtown District', 'New York', 'NY', 'United States', '10001',
     40.7128, -74.0060, 5.0, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'PARK', 'Park View Resort', 'Scenic resort with lake views',
     '250 Park Avenue', 'Lakeside', 'Denver', 'CO', 'United States', '80202',
     39.7392, -104.9903, 4.5, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW()),
    (nextval('primary_sequence'), 'BEACH', 'Beachside Paradise Hotel', 'Tropical beachfront hotel with water sports',
     '500 Ocean Drive', 'Beach Front', 'Miami', 'FL', 'United States', '33139',
     25.7617, -80.1918, 4.0, 'ACTIVE', NOW(), NOW(), 1, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ============================================================================
-- SAMPLE ROOMS
-- ============================================================================

-- Plaza Hotel rooms
INSERT INTO rooms (id, hotel_id, room_type_id, room_no, floor_no, status, smoking_allowed, created_at, updated_at, version, date_created, last_updated)
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PLZA'),
    (SELECT id FROM room_types WHERE code = 'SINGLE'),
    '101', 1, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW()
UNION ALL
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PLZA'),
    (SELECT id FROM room_types WHERE code = 'DOUBLE'),
    '102', 1, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW()
UNION ALL
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PLZA'),
    (SELECT id FROM room_types WHERE code = 'SUITE'),
    '201', 2, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW()
UNION ALL
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PLZA'),
    (SELECT id FROM room_types WHERE code = 'DELUXE'),
    '202', 2, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW();

-- Park View Resort rooms
INSERT INTO rooms (id, hotel_id, room_type_id, room_no, floor_no, status, smoking_allowed, created_at, updated_at, version, date_created, last_updated)
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PARK'),
    (SELECT id FROM room_types WHERE code = 'TWIN'),
    '105', 1, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW()
UNION ALL
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'PARK'),
    (SELECT id FROM room_types WHERE code = 'DOUBLE'),
    '106', 1, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW();

-- Beachside Paradise Hotel rooms
INSERT INTO rooms (id, hotel_id, room_type_id, room_no, floor_no, status, smoking_allowed, created_at, updated_at, version, date_created, last_updated)
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'BEACH'),
    (SELECT id FROM room_types WHERE code = 'DOUBLE'),
    '201', 2, 'AVAILABLE', FALSE, NOW(), NOW(), 1, NOW(), NOW()
UNION ALL
SELECT
    nextval('primary_sequence'),
    (SELECT id FROM hotels WHERE code = 'BEACH'),
    (SELECT id FROM room_types WHERE code = 'SUITE'),
    '301', 3, 'AVAILABLE', TRUE, NOW(), NOW(), 1, NOW(), NOW();

-- ============================================================================
-- SAMPLE ROOM AMENITIES
-- ============================================================================

-- Add amenities to all rooms
INSERT INTO room_amenities (room_id, amenity_id)
SELECT r.id, a.id
FROM rooms r
CROSS JOIN amenities a
WHERE a.code IN ('WIFI', 'TV', 'AC', 'SAFE', 'PHONE')
ON CONFLICT DO NOTHING;

-- Add premium amenities to deluxe and suite rooms
INSERT INTO room_amenities (room_id, amenity_id)
SELECT r.id, a.id
FROM rooms r
JOIN room_types rt ON r.room_type_id = rt.id
CROSS JOIN amenities a
WHERE rt.code IN ('SUITE', 'DELUXE')
AND a.code IN ('MINI_BAR', 'HAIRDRYER', 'BATHROBE', 'DESK', 'KETTLE')
ON CONFLICT DO NOTHING;

-- ============================================================================
-- AUDIT LOG
-- ============================================================================

-- Print message to indicate successful data insertion
SELECT 'StayHub initial data loaded successfully!' as status;

-- ============================================================================
-- END OF DATA MIGRATION SCRIPT
-- ============================================================================

