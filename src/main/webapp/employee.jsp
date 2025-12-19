<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Employee Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<!-- <div class="navbar">
    <h1>Employee Portal</h1>
     <a href="<%= request.getContextPath() %>/logout">Logout</a>
</div> -->
<div class="navbar">
    <h1>Employee Portal</h1>
    
    <div class="nav-actions">
        <a href="<%= request.getContextPath() %>/home">Home</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
        <!-- <a href="<%= request.getContextPath() %>/home">Back to Home</a> -->
    </div>
</div>


<div class="container">
    <%
        String userName = (String) request.getAttribute("userName");
    %>

    
    <div class="welcome-box">
        <h3>Welcome <strong><%= userName %></strong></h3>
    </div>

    <%
        Integer loggedUserId = (Integer) session.getAttribute("userId");

        java.util.List<com.leavemanagement.servlet.LeaveService.LeaveRequestData> leaves =
            (java.util.List<com.leavemanagement.servlet.LeaveService.LeaveRequestData>)
                request.getAttribute("leaveRequests");
    %>
                <h2>Your Leave Requests</h2>

<table>
    <thead>
        <tr>
            <th>Days</th>
            <th>Status</th>
            <th>Approved By</th>
            <th>Decline Reason</th>
        </tr>
    </thead>
    <tbody>
    <%
        if (leaves != null && loggedUserId != null) {
            for (var l : leaves) {
                if (l.getUserId() == loggedUserId) {
    %>
        <tr>
            <td><%= l.getLeaveDays() %></td>
            <td><%= l.getLeaveStatus() %></td>
            <td>
                <%= l.getApprovedBy() != null ? l.getApprovedBy() : "-" %>
            </td>
            <td>
                <%= l.getDeclineComment() != null ? l.getDeclineComment() : "-" %>
            </td>
        </tr>
    <%
                }
            }
        }
    %>
    </tbody>
</table>

    <!-- <div class="nav-links">
    </div> -->

    <% String message = (String) request.getAttribute("message"); %>
    <% String messageType = (String) request.getAttribute("messageType"); %>

    <% if (message != null) { %>
        <div class="alert <%= "success".equals(messageType) ? "alert-success" : "alert-error" %>">
            <%= message %>
        </div>
    <% } %>

    <div class="forms-wrapper">

        <!-- APPLY LEAVE -->
        <form method="POST"
              action="<%= request.getContextPath() %>/employee"
              class="form-section apply-leave-card">

            <h2>Apply for Leave</h2>

            <label>Number of Days</label>
            <input type="number" name="days" min="1" max="30" required>
            <label>Leave Type</label>
            <select name="leaveType" required>
                <option value="">-- Select Leave Type --</option>
                <option value="SICK">Sick Leave</option>
                <option value="CASUAL">Casual Leave</option>
            </select>
            <label>Leave Date</label>
            <input type="date" name="leaveDate" required>


            <label>Reason for Leave</label>
            <textarea name="reason" rows="3" required></textarea>

            <input type="hidden" name="action" value="apply">
            <button type="submit">Submit Leave Request</button>
        </form>

        <!-- CHECK BALANCE -->
        <form method="POST"
              action="<%= request.getContextPath() %>/employee"
              class="form-section  apply-leave-card">

            <h2>Check Leave Balance</h2>

            <input type="hidden" name="action" value="balance">
            <button type="submit">Check Balance</button>

            <% Integer balance = (Integer) request.getAttribute("balance"); %>
            <% if (balance != null) { %>
                <div class="balance-result">
                    Remaining Leave Days:
                    <strong><%= balance %></strong>
                </div>
            <% } %>

        </form>

    </div>
</div>

</body>
</html>
