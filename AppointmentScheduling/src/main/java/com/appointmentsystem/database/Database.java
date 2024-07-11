package com.appointmentsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static String url = "jdbc:postgresql://localhost:5432/Appointment_Database"; // Replace with your database URL
    private static String user = "postgres"; // Replace with your username
    private static String password = "vamsi9999"; // Replace with your password

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found. Include it in your library path.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
