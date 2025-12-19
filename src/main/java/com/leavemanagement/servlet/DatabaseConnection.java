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
package com.leavemanagement.servlet; // keep YOUR package name

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:postgresql://localhost:5432/leave_management";

    private static final String USER = "postgres";
    private static final String PASSWORD = "kavi"; // your pg password

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PostgreSQL connection failed");
        }
    }
}

