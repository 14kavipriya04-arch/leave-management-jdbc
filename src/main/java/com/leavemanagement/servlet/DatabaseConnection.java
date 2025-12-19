package com.leavemanagement.servlet;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    // TEMPORARY: hardcoded Neon DB for testing
    // Once stable, you can switch back to DATABASE_URL env
    private static final String JDBC_URL =
        "jdbc:postgresql://ep-solitary-glade-adhnsg1e-pooler.c-2.us-east-1.aws.neon.tech/neondb"
      + "?user=neondb_owner"
      + "&password=npg_pno6QfFe9axk"
      + "&sslmode=require"
      + "&channelBinding=require";

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(JDBC_URL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed");
        }
    }
}
