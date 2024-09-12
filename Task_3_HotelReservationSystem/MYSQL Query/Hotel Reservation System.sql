CREATE DATABASE Hotel_Reservation_System;
USE Hotel_Reservation_System;

CREATE TABLE users( 
id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
name VARCHAR(50) NOT NULL,
password VARCHAR(50) NOT NULL,
email VARCHAR(100) NOT NULL,
contact VARCHAR(20) NOT NULL);
SELECT * FROM users;

CREATE TABLE rooms (
    room_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,   -- Unique identifier for each room
    room_number VARCHAR(10) UNIQUE NOT NULL,          -- Room Number (Unique Identifier)
    room_type VARCHAR(50),                            -- Type of Room (e.g., Single, Double, suite)
    price_per_night DECIMAL(10, 2),                   -- Price per Night                                    -- Maximum Capacity of the Room
    availability_status ENUM('Available', 'Booked')  -- Status (Available, Booked)
);

INSERT INTO rooms (room_number, room_type, price_per_night, availability_status) VALUES
('201', 'Single', 5000.00, 'Available'),
('202', 'Single', 5000.00, 'Available'),
('203', 'Single', 5000.00, 'Available'),
('301', 'Double', 8000.00, 'Available'),
('302', 'Double', 8000.00, 'Available'),
('303', 'Double', 8000.00, 'Available'),
('401', 'Suite', 12000.00, 'Available'),
('402', 'Suite', 12000.00, 'Available'),
('403', 'Suite', 12000.00, 'Available'),
('404', 'Suite', 12000.00, 'Available'),
('501', 'Single', 5000.00, 'Available'),
('502', 'Single', 5000.00, 'Available'),
('503', 'Double', 8000.00, 'Available'),
('504', 'Double', 8000.00, 'Available'),
('505', 'Suite', 12000.00, 'Available');
SELECT * FROM rooms;

CREATE INDEX idx_room_number ON rooms (room_number);

CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    room_number VARCHAR(10) NOT NULL,  -- Room number to link to the specific room
    room_type VARCHAR(20) NOT NULL,
    original_price DECIMAL(10, 2) NOT NULL,
    discount_applied BOOLEAN NOT NULL,
    discounted_price DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_status ENUM('Pending', 'Completed') NOT NULL,
    offer_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)  -- Foreign key constraint
);

SELECT * FROM Reservations;