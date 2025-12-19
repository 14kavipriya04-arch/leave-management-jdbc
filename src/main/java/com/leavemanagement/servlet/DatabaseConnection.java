// package com.leavemanagement.servlet;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DatabaseConnection {

//     private static final String URL =
//             "jdbc:mysql://localhost:3306/leave_management?useSSL=false&serverTimezone=UTC";
//     private static final String USER = "kavi";
//     private static final String PASSWORD = "kavi";

//     public static Connection getConnection() {
//         try {
//             Class.forName("com.mysql.cj.jdbc.Driver");
//             return DriverManager.getConnection(URL, USER, PASSWORD);
//         } catch (Exception e) {
//             throw new RuntimeException("DB connection failed", e);
//         }
//     }
// }
package com.leavemanagement.servlet;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String DB_URL =
            System.getenv("DATABASE_URL");

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed");
        }
    }
}
