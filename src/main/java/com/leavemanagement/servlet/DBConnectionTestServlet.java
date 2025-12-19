package com.leavemanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;

@WebServlet("/db-test")
public class DBConnectionTestServlet extends HttpServlet {

    private static final String JDBC_URL =
        "jdbc:postgresql://ep-solitary-glade-adhnsg1e-pooler.c-2.us-east-1.aws.neon.tech/neondb"
      + "?user=neondb_owner"
      + "&password=npg_pno6QfFe9axk"
      + "&sslmode=require"
      + "&channelBinding=require";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        out.println("Testing PostgreSQL connection...");

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL);

            out.println("✅ DATABASE CONNECTION SUCCESS");
            out.println("Connected to: " + con.getMetaData().getDatabaseProductName());

            con.close();
        } catch (Exception e) {
            out.println("❌ DATABASE CONNECTION FAILED");
            e.printStackTrace(out);
        }
    }
}
