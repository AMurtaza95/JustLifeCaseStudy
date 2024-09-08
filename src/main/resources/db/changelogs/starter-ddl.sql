-- liquibase formatted sql

-- changeset author:001-create-vehicle-table
SELECT 'Creating Vehicle Table' AS log_message;
CREATE TABLE vehicle (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at timestamp DEFAULT NOW() NOT NULL,
    updated_at timestamp DEFAULT NOW() NOT NULL,
    CONSTRAINT PK_VEHICLE PRIMARY KEY (id)
);

-- changeset author:002-create-cleaner-table
CREATE TABLE cleaner (
    id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    vehicle_id VARCHAR(36) NOT NULL,
    created_at timestamp DEFAULT NOW() NOT NULL,
    updated_at timestamp DEFAULT NOW() NOT NULL,
    CONSTRAINT PK_CLEANER PRIMARY KEY (id)
);

ALTER TABLE cleaner
ADD CONSTRAINT FK_CLEANER_VEHICLE FOREIGN KEY (vehicle_id) REFERENCES vehicle(id);

-- changeset author:003-create-booking-table
CREATE TABLE booking (
    id VARCHAR(36) NOT NULL,
    start_date_time DATETIME NOT NULL,
    end_date_time DATETIME NOT NULL,
    duration INT NOT NULL,
    created_at timestamp DEFAULT NOW() NOT NULL,
    updated_at timestamp DEFAULT NOW() NOT NULL,
    CONSTRAINT PK_BOOKING PRIMARY KEY (id)
);

-- changeset author:004-create-booking-cleaner-table
CREATE TABLE booking_cleaner (
    booking_id VARCHAR(36) NOT NULL,
    cleaner_id VARCHAR(36) NOT NULL,
    created_at timestamp DEFAULT NOW() NOT NULL,
    updated_at timestamp DEFAULT NOW() NOT NULL,
    CONSTRAINT PK_BOOKING_CLEANER PRIMARY KEY (booking_id, cleaner_id)
);

ALTER TABLE booking_cleaner
ADD CONSTRAINT FK_BOOKING_CLEANER_BOOKING FOREIGN KEY (booking_id) REFERENCES booking(id);

ALTER TABLE booking_cleaner
ADD CONSTRAINT FK_BOOKING_CLEANER_CLEANER FOREIGN KEY (cleaner_id) REFERENCES cleaner(id);
