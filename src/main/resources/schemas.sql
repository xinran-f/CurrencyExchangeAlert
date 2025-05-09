DROP DATABASE IF EXISTS exchange_tracker;
CREATE DATABASE exchange_tracker;
USE exchange_tracker;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       id INT AUTO_INCREMENT NOT NULL,
                       username   VARCHAR(25)   NOT NULL,
                       `password` VARCHAR(25)   NOT NULL,
                       email      VARCHAR(255)  NOT NULL,
                       created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT pk_user PRIMARY KEY (id)
) ENGINE=InnoDB CHARSET=utf8mb4;

-- seed one user so user_id = 1 is valid
INSERT INTO users (username, `password`, email)
VALUES ('alice','secret','alice@example.com');

DROP TABLE IF EXISTS exchange_alert;
CREATE TABLE exchange_alert (
                                id            INT             AUTO_INCREMENT PRIMARY KEY,
                                user_id       INT             NOT NULL,
                                user_email    VARCHAR(255)    NOT NULL,
                                base_currency VARCHAR(10)     NOT NULL,
                                target_currency VARCHAR(10)   NOT NULL,
                                target_rate   DECIMAL(10,4)   NOT NULL,
                                notified      BOOLEAN         NOT NULL DEFAULT FALSE,
                                created_at    TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_user FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE
) ENGINE=InnoDB CHARSET=utf8mb4;
