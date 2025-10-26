CREATE DATABASE IF NOT EXISTS iot_honeypot;

USE iot_honeypot;

CREATE TABLE IF NOT EXISTS attacks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50),
    description VARCHAR(255),
    severity VARCHAR(20),
    ip VARCHAR(50),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
