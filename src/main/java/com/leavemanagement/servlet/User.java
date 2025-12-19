package com.leavemanagement.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int userId;
    private String name;
    private String role;
    private int leaveBalance;
    private String password;

    /* ---------- CONSTRUCTOR ---------- */

    public User(int userId, String name, String role, int leaveBalance, String password) {
        this.userId = userId;
        this.name = name;
        this.role = role;
        this.leaveBalance = leaveBalance;
        this.password = password;
    }

    /* ---------- LOAD SINGLE USER ---------- */

    public static User loadFromDB(int userId) {

        String sql =
            "SELECT userId, name, role, leave_balance, password " +
            "FROM users WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getInt("leave_balance"),
                        rs.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ---------- LOGIN ---------- */

    public static User login(int userId, String password) {

        String sql =
            "SELECT userId, name, role, leave_balance, password " +
            "FROM users WHERE userId = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getInt("leave_balance"),
                        rs.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ---------- UPDATE LEAVE BALANCE ---------- */

    public boolean saveToDB() {

        String sql = "UPDATE users SET leave_balance = ? WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, leaveBalance);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateLeaveBalanceInDB() throws SQLException {

        String sql = "UPDATE users SET leave_balance = ? WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, leaveBalance);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    /* ---------- FETCH ALL USERS ---------- */

    public static List<User> getAllEmployees() {

        List<User> users = new ArrayList<>();

        String sql =
            "SELECT userId, name, role, leave_balance, password FROM users";

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getInt("leave_balance"),
                    rs.getString("password")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /* ---------- GETTERS ---------- */

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getLeaveBalance() { return leaveBalance; }
    public String getPassword() { return password; }

    /* ---------- BUSINESS ---------- */

    public void setLeaveBalance(int days) {
        this.leaveBalance = days;
        saveToDB();
    }
}
