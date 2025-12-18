<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Portal</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>

<body>

<div class="navbar">
    <h1>Manager Portal</h1>

    <div class="nav-actions">
        <a href="<%= request.getContextPath() %>/home">Home</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </div>
</div>


<div class="container">
    <%
        if (session == null || session.getAttribute("userName") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
    %>

   <%
        String userName = (String) session.getAttribute("userName");
    %>


    
    <div class="welcome-box">
        <h3>Welcome <strong><%= userName %></strong></h3>
    </div>

    <!-- Navigation -->
    <!-- <div class="nav-links">
        <a href="<%= request.getContextPath() %>/home">Home</a>
      <a href="<%= request.getContextPath() %>/manager">Refresh</a> 
        <a href="<%= request.getContextPath() %>/logout">Logout</a>

    </div> -->

    <!-- Message -->
    <%
        String message = (String) request.getAttribute("message");
        String messageType = (String) request.getAttribute("messageType");
        if (message != null) {
    %>
        <div class="alert <%= "success".equals(messageType) ? "alert-success" : "alert-error" %>">
            <%= message %>
        </div>
    <% } %>

    <!-- Leave Requests Table -->
    <div class="section">
        <h2>All Leave Requests</h2>
        <p>Pending requests can be approved or denied. Approved/Denied requests are read-only.</p>

        <table>
            <thead>
                <tr>
                    <th>Request ID</th>
                    <th>Employee ID</th>
                    <th>Leave Days</th>
                    <th>Status</th>
                    <th>Apply Reason</th>
                    <th>Decline Comment</th>
                    <th>Actions</th>
                </tr>
            </thead>

            <tbody>
            <%
                java.util.List pending =
                    (java.util.List) request.getAttribute("pendingRequests");

                if (pending != null && !pending.isEmpty()) {
                    for (Object o : pending) {
                        com.leavemanagement.servlet.LeaveService.LeaveRequestData r =
                            (com.leavemanagement.servlet.LeaveService.LeaveRequestData) o;
            %>
                <tr>
                    <td><%= r.getLeaveRequestId() %></td>
                    <td><%= r.getUserId() %></td>
                    <td><%= r.getLeaveDays() %></td>
                    <td><%= r.getLeaveStatus() %></td>
                    <td><%= r.getApplyReason() %></td>
                    <td>
                        <%= r.getDeclineComment() == null ? "-" : r.getDeclineComment() %>
                    </td>

                    <td>
                        <% if ("Pending".equalsIgnoreCase(r.getLeaveStatus())) { %>

                            <!-- APPROVE -->
                            <form method="POST" action="<%= request.getContextPath() %>/manager"
                                  style="display:inline">
                                <input type="hidden" name="requestId"
                                       value="<%= r.getLeaveRequestId() %>">
                                <input type="hidden" name="action" value="approve">
                                <button type="submit" class="btn-success">
                                    Approve
                                </button>
                            </form>

                            <!-- DENY WITH COMMENT -->
                            <form method="POST" action="<%= request.getContextPath() %>/manager"
                                  style="display:inline; margin-left:6px;">
                                <input type="hidden" name="requestId"
                                       value="<%= r.getLeaveRequestId() %>">
                                <input type="hidden" name="action" value="deny">

                                <input type="text"
                                       name="comment"
                                       placeholder="Enter decline reason"
                                       required
                                       style="width:180px; margin-right:6px;">

                                <button type="submit" class="btn-danger">
                                    Deny
                                </button>
                            </form>

                        <% } else { %>
                            -
                        <% } %>
                    </td>
                </tr>

            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="7">No leave requests found</td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
