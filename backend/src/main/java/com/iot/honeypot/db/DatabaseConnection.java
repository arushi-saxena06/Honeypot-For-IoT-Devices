package com.iot.honeypot.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/iot_honeypot";
    private static final String USER = "root"; // your DB user
    private static final String PASSWORD = "root"; // your DB password

    private static Connection conn;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database connected successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Database connection failed!");
            }
        }
        return conn;
    }
}
