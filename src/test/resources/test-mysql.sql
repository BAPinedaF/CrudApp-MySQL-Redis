BEGIN;

CREATE TABLE IF NOT EXISTS CUSTOMERS(
	ID BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(255),
    LASTNAME VARCHAR(255),
    PHONE VARCHAR(50)
);

TRUNCATE TABLE CUSTOMERS;

INSERT INTO CUSTOMERS VALUES 
('Bruce', 'Wayne', '666666'),
('Diana', 'Prince', '1111111'),
('Clark', 'Kent', '5555555');



COMMIT;