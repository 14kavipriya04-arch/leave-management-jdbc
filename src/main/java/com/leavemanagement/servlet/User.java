package com.leavemanagement.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class User {

    private int userId;
    private String name;
    private String role;
    private int leaveBalance;

    private String password;

    public User(int userId, String name, String role, int leaveBalance, String password) {
    this.userId = userId;
    this.name = name;
    this.role = role;
    this.leaveBalance = leaveBalance;
    this.password = password;
}


    /* ---------- DB Operations ---------- */

    public static User loadFromDB(int userId) {

        String sql = "SELECT * FROM users WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("userId"),
                            rs.getString("name"),
                            rs.getString("role"),
                            rs.getInt("leaveBalance"),
                             rs.getString("password")
                    );
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean saveToDB() {

        String sql = "UPDATE users SET leaveBalance = ? WHERE userId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, leaveBalance);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    /* ---------- Getters ---------- */

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    /* ---------- Business ---------- */

    public void setLeaveBalance(int days) {
        this.leaveBalance = days;
        saveToDB();
    }

    public void updateLeaveBalanceInDB() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps =
            conn.prepareStatement("UPDATE users SET leaveBalance=? WHERE userId=?");
        ps.setInt(1, leaveBalance);
        ps.setInt(2, userId);
        ps.executeUpdate();
    }
    public static List<User> getAllEmployees() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT userId, name, role, leaveBalance FROM users";

        try (Connection con = DatabaseConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getInt("leaveBalance"),
                    rs.getString("password")
                );
                users.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public static User login(int userId, String password) {

        String sql = "SELECT * FROM users WHERE userId=? AND password=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("userId"),
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getInt("leaveBalance"),
                    rs.getString("password")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
