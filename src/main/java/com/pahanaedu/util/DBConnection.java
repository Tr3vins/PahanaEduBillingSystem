package com.pahanaedu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Utility class for managing database connection.

public class DBConnection {
    // Database credentials and URL
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false";
    private static final String JDBC_USERNAME = "root"; // Replace with your MySQL username
    private static final String JDBC_PASSWORD = "pass"; // Replace with your MySQL password


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }


    public static void closeResources(AutoCloseable connection, AutoCloseable statement, AutoCloseable resultSet) {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}