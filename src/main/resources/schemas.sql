DROP DATABASE IF EXISTS exchange_tracker;
CREATE DATABASE exchange_tracker;

USE exchange_tracker;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id INT AUTO_INCREMENT NOT NULL,
                       username VARCHAR(25) NOT NULL,
                       `password` VARCHAR(25) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       CONSTRAINT pk_user PRIMARY KEY (id),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS exchange_alerts;
CREATE TABLE exchange_alert (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                user_email VARCHAR(255) NOT NULL,
                                base_currency VARCHAR(10) NOT NULL,
                                target_currency VARCHAR(10) NOT NULL,
                                target_rate DECIMAL(10,4) NOT NULL,
                                notified BOOLEAN DEFAULT FALSE
);
