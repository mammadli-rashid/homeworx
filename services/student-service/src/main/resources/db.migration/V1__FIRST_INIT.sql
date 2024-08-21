CREATE TABLE students
(
    student_id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name               VARCHAR(255)                        NOT NULL,
    date_of_birth           DATE,
    email                   VARCHAR(255)                        NOT NULL,
    primary_mobile_number   VARCHAR(20)                         NOT NULL,
    secondary_mobile_number VARCHAR(20),
    address                 TEXT,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modified_at        TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    active_status           INT       DEFAULT 1                 NOT NULL
);
