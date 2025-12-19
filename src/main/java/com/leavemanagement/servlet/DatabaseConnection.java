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

    public static Connection getConnection() {
        try {
            String dbUrl = System.getenv("DATABASE_URL");

            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL not set");
            }

            // 🔥 IMPORTANT: Convert Render URL to JDBC URL
            if (dbUrl.startsWith("postgresql://")) {
                dbUrl = dbUrl.replace("postgresql://", "jdbc:postgresql://");
            }

            // 🔐 Ensure SSL (Render requires this)
            if (!dbUrl.contains("sslmode")) {
                dbUrl += "?sslmode=require";
            }

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed");
        }
    }
}

