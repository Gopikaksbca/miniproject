package com.lastproject.servlets;

import java.sql.*;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/voting_db";
    private static final String USER = "root";       // your MySQL username
    private static final String PASSWORD = "password"; // your MySQL password

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
