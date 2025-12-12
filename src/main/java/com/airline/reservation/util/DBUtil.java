package com.airline.reservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Use environment variables if available, else defaults
    private static final String URL = System.getenv().getOrDefault(
            "AIRLINE_DB_URL",
            "jdbc:postgresql://localhost:5432/com.airline"
    );

    private static final String USER = System.getenv().getOrDefault(
            "AIRLINE_DB_USER",
            "postgres"
    );

    private static final String PASS = System.getenv().getOrDefault(
            "AIRLINE_DB_PASS",
            "Admin123"
    );

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ex) {
            System.err.println("DB connection failed: " + ex.getMessage());
            throw ex;
        }
    }
}

