-- liquibase formatted sql

-- changeset author:001-create-vehicle-table
SELECT 'Creating Vehicle Table' AS log_message;
CREATE TABLE vehicle (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_VEHICLE PRIMARY KEY (id)
);

-- changeset author:002-create-cleaner-table
SELECT 'Creating Cleaner Table' AS log_message;
CREATE TABLE cleaner (
    id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    vehicle_id VARCHAR(36) NOT NULL,
    CONSTRAINT PK_CLEANER PRIMARY KEY (id)
);

ALTER TABLE cleaner
ADD CONSTRAINT FK_CLEANER_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle(id);

-- changeset author:003-create-booking-table
SELECT 'Creating Booking Table' AS log_message;
CREATE TABLE booking (
    id VARCHAR(36) NOT NULL,
    start_date_time DATETIME NOT NULL,
    duration INT NOT NULL,
    CONSTRAINT PK_BOOKING PRIMARY KEY (id)
);

-- changeset author:004-create-booking-cleaner-table
SELECT 'Creating Booking_Cleaner Table' AS log_message;
CREATE TABLE booking_cleaner (
    booking_id VARCHAR(36) NOT NULL,
    cleaner_id VARCHAR(36) NOT NULL,
    CONSTRAINT PK_BOOKING_CLEANER PRIMARY KEY (booking_id, cleaner_id)
);

ALTER TABLE booking_cleaner
ADD CONSTRAINT FK_BOOKING_CLEANER_BOOKING FOREIGN KEY (booking_id) REFERENCES booking(id);

ALTER TABLE booking_cleaner
ADD CONSTRAINT FK_BOOKING_CLEANER_CLEANER FOREIGN KEY (cleaner_id) REFERENCES cleaner(id);

-- changeset author:005-insert-vehicle
SELECT 'Inserting Vehicle Table' AS log_message;
INSERT INTO vehicle (id, name) VALUES
('1', 'Vehicle 1'),
('2', 'Vehicle 2'),
('3', 'Vehicle 3'),
('4', 'Vehicle 4'),
('5', 'Vehicle 5');

-- changeset author:006-insert-cleaner
SELECT 'Inserting Cleaner Table' AS log_message;
INSERT INTO cleaner (id, name, vehicle_id) VALUES
('1', 'Cleaner 1', '1'),
('2', 'Cleaner 2', '1'),
('3', 'Cleaner 3', '1'),
('4', 'Cleaner 4', '1'),
('5', 'Cleaner 5', '1'),
('6', 'Cleaner 6', '2'),
('7', 'Cleaner 7', '2'),
('8', 'Cleaner 8', '2'),
('9', 'Cleaner 9', '2'),
('10', 'Cleaner 10', '2'),
('11', 'Cleaner 11', '3'),
('12', 'Cleaner 12', '3'),
('13', 'Cleaner 13', '3'),
('14', 'Cleaner 14', '3'),
('15', 'Cleaner 15', '3'),
('16', 'Cleaner 16', '4'),
('17', 'Cleaner 17', '4'),
('18', 'Cleaner 18', '4'),
('19', 'Cleaner 19', '4'),
('20', 'Cleaner 20', '4'),
('21', 'Cleaner 21', '5'),
('22', 'Cleaner 22', '5'),
('23', 'Cleaner 23', '5'),
('24', 'Cleaner 24', '5'),
('25', 'Cleaner 25', '5');
