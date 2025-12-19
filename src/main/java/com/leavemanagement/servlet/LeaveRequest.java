package com.leavemanagement.servlet;

// import com.leavemanagement.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LeaveRequest {

    private int leaveRequestId;
    private int userId;
    private String leaveStatus;
    private int leaveDays;
    private String applyReason;
    private String declineComment;

    private String approvedBy;

    private String leaveType;     // SICK or CASUAL
    private java.sql.Date leaveDate;

        public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public java.sql.Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(java.sql.Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }


    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LeaveRequest(int userId, int leaveDays) {
        this.userId = userId;
        this.leaveDays = leaveDays;
        this.leaveStatus = "Pending";
    }


    public LeaveRequest(int userId, int leaveDays,
                    String applyReason, String leaveType,
                    java.sql.Date leaveDate) {

        this.userId = userId;
        this.leaveDays = leaveDays;
        this.applyReason = applyReason;
        this.leaveType = leaveType;
        this.leaveDate = leaveDate;
        this.leaveStatus = "Pending";
    }


    private LeaveRequest(int leaveRequestId, int userId,
                         String leaveStatus, int leaveDays) {
        this.leaveRequestId = leaveRequestId;
        this.userId = userId;
        this.leaveStatus = leaveStatus;
        this.leaveDays = leaveDays;
    }

    /* ---------- DB Operations ---------- */

    public boolean save() {
        String sql =
                "INSERT INTO leave_requests (userId, leaveStatus, leaveDays, apply_reason, leave_type, leave_date) " +
                "VALUES (?, ?, ? ,?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, leaveStatus);
            stmt.setInt(3, leaveDays);
            stmt.setString(4, applyReason);
            stmt.setString(5, leaveType);
            stmt.setDate(6, leaveDate);
            int rows = stmt.executeUpdate();
            if (rows == 0) return false;

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.leaveRequestId = rs.getInt(1);
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public static LeaveRequest loadFromDB(int requestId) {
        String sql = "SELECT * FROM leave_requests WHERE leaveRequestId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeaveRequest lr = new LeaveRequest(
                            rs.getInt("leaveRequestId"),
                            rs.getInt("userId"),
                            rs.getString("leaveStatus"),
                            rs.getInt("leaveDays")
                    );
                    lr.applyReason = rs.getString("apply_reason");
                    lr.declineComment = rs.getString("decline_comment");
                    lr.approvedBy = rs.getString("approved_by");
                    return lr;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public boolean updateStatus(String status) {
        this.leaveStatus = status;

        String sql =
                "UPDATE leave_requests SET leaveStatus = ? WHERE leaveRequestId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, leaveRequestId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }
    public boolean decline(String comment) {

        this.leaveStatus = "Denied";
        this.declineComment = comment;

        String sql =
            "UPDATE leave_requests SET leaveStatus = ?, decline_comment = ? " +
            "WHERE leaveRequestId = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, leaveStatus);
            stmt.setString(2, declineComment);
            stmt.setInt(3, leaveRequestId);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }
    public boolean approve(String managerName) throws Exception {
        String sql =
            "UPDATE leave_requests SET leaveStatus='Approved', approved_by=? WHERE leaveRequestId=?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, managerName);
            ps.setInt(2, leaveRequestId);

            return ps.executeUpdate() > 0;
        }
    }

    /* ---------- Getters ---------- */

    public int getLeaveRequestId() {
        return leaveRequestId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public int getLeaveDays() {
        return leaveDays;
    }
}
