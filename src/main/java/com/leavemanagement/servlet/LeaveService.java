package com.leavemanagement.servlet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;

public class LeaveService {

    public OperationResult applyLeave(User user, int days, String reason)
{

    if (user == null) {
        return new OperationResult(false, "User not found");
    }

    if (!"Employee".equals(user.getRole())) {
        return new OperationResult(false, "Only employees can apply leave");
    }

    int alreadyRequested = getTotalRequestedLeaves(user.getUserId());
    int totalAfterRequest = alreadyRequested + days;

    if (totalAfterRequest > user.getLeaveBalance()) {
        return new OperationResult(
            false,
            "You already requested " + alreadyRequested +
            " days. Cannot request " + days +
            " more days. Leave Total is only " +
            user.getLeaveBalance()
        );
    }

    if (reason == null || reason.trim().isEmpty()) {
        return new OperationResult(false, "Reason is mandatory to apply leave");
    }

    
    LeaveRequest lr = new LeaveRequest(user.getUserId(), days, reason);
    boolean saved = lr.save();


    return saved
            ? new OperationResult(true, "Leave request submitted (Pending approval)")
            : new OperationResult(false, "Failed to submit leave request");
}


    public List<LeaveRequestData> getPendingLeaves() {

        List<LeaveRequestData> pendingRequests = new ArrayList<>();

        String sql =
            "SELECT leaveRequestId, userId, leaveDays, leaveStatus, apply_reason, decline_comment , approved_by " +
            "FROM leave_requests";


        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LeaveRequestData req = new LeaveRequestData();
                req.setLeaveRequestId(rs.getInt("leaveRequestId"));
                req.setUserId(rs.getInt("userId"));
                req.setLeaveDays(rs.getInt("leaveDays"));
                req.setLeaveStatus(rs.getString("leaveStatus"));
                req.setApplyReason(rs.getString("apply_reason"));
                req.setDeclineComment(rs.getString("decline_comment"));

                req.setApprovedBy(rs.getString("approved_by"));

                pendingRequests.add(req);
            }

        } catch (Exception e) {
            // return empty list on failure
        }

        return pendingRequests;
    }

    public OperationResult approveLeave(int requestId, String managerName) {
        try {
            LeaveRequest lr = LeaveRequest.loadFromDB(requestId);

            if (lr == null)
                return new OperationResult(false, "Request not found");

            if (!"Pending".equalsIgnoreCase(lr.getLeaveStatus()))
                return new OperationResult(false, "Already processed");

            boolean approved = lr.approve(managerName);

            if (!approved)
                return new OperationResult(false, "Failed to approve leave");

            return new OperationResult(true, "Leave approved successfully");

        } catch (Exception e) {
            return new OperationResult(false, "Error: " + e.getMessage());
        }
    }

    public OperationResult declineLeave(int requestId, String comment) {

        if (comment == null || comment.trim().isEmpty()) {
            return new OperationResult(false, "Comment is mandatory to decline leave");
        }

        try {
            LeaveRequest lr = LeaveRequest.loadFromDB(requestId);

            if (lr == null)
                return new OperationResult(false, "Request not found");

            if (!"Pending".equals(lr.getLeaveStatus()))
                return new OperationResult(false, "Already processed");

            boolean updated = lr.decline(comment);

            return updated
                    ? new OperationResult(true, "Leave request declined successfully")
                    : new OperationResult(false, "Unable to decline leave");

        } catch (Exception e) {
            return new OperationResult(false, "Error while declining leave");
        }
    }
    public int getTotalRequestedLeaves(int userId) {
        int total = 0;

        String sql = """
            SELECT COALESCE(SUM(leaveDays), 0)
            FROM leave_requests
            WHERE userId = ?
            AND leaveStatus IN ('Pending', 'Approved')
        """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public int getRemainingLeaves(int userId) {

        User user = User.loadFromDB(userId);
        if (user == null) {
            return 0;
        }

        int totalLeave = user.getLeaveBalance();
        int usedOrPending = getTotalRequestedLeaves(userId);

        int remaining = totalLeave - usedOrPending;

        return Math.max(remaining, 0); // never return negative
    }


    /* -------- Simple DTO -------- */
    public static class LeaveRequestData {

        private int leaveRequestId;
        private int userId;
        private int leaveDays;
        private String leaveStatus;
        private String applyReason;
        private String declineComment;
        private String approvedBy;



        public int getLeaveRequestId() {
            return leaveRequestId;
        }

        public void setLeaveRequestId(int leaveRequestId) {
            this.leaveRequestId = leaveRequestId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getLeaveDays() {
            return leaveDays;
        }

        public void setLeaveDays(int leaveDays) {
            this.leaveDays = leaveDays;
        }
        public String getLeaveStatus() {
            return leaveStatus;
        }

        public void setLeaveStatus(String leaveStatus) {
            this.leaveStatus = leaveStatus;
        }

        public String getApplyReason() {
            return applyReason;
        }

        public void setApplyReason(String applyReason) {
            this.applyReason = applyReason;
        }

        public String getDeclineComment() {
            return declineComment;
        }

        public void setDeclineComment(String declineComment) {
            this.declineComment = declineComment;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }


    }
}
