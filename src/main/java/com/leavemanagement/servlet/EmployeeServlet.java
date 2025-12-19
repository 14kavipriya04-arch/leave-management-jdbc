package com.leavemanagement.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class EmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("role") == null ||
            !"EMPLOYEE".equalsIgnoreCase(session.getAttribute("role").toString())) {

            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // List<User> users = User.getAllEmployees();
        // request.setAttribute("users", users);
        request.setAttribute("userName",
                session != null ? session.getAttribute("userName") : null);

        LeaveService service = new LeaveService();
        List<LeaveService.LeaveRequestData> leaves =
                service.getPendingLeaves();

        request.setAttribute("leaveRequests", leaves);

        request.getRequestDispatcher("/employee.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("role") == null ||
            !"EMPLOYEE".equalsIgnoreCase(session.getAttribute("role").toString())) {

            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
            request.setAttribute("userName",
                    session != null ? session.getAttribute("userName") : null);

        String action = request.getParameter("action");

        if ("apply".equals(action)) {
            applyLeave(request);
        } else if ("balance".equals(action)) {
            viewBalance(request);
        }

        // List<User> users = User.getAllEmployees();
        // request.setAttribute("users", users);

        LeaveService service = new LeaveService();
        List<LeaveService.LeaveRequestData> leaves =
                service.getPendingLeaves();

        request.setAttribute("leaveRequests", leaves);

        request.getRequestDispatcher("/employee.jsp")
               .forward(request, response);
    }

    private void applyLeave(HttpServletRequest request) {
        try {
            String leaveType = request.getParameter("leaveType");
            java.sql.Date leaveDate =
                    java.sql.Date.valueOf(request.getParameter("leaveDate"));

            HttpSession session = request.getSession(false);
            int userId = (int) session.getAttribute("userId");

            int days = Integer.parseInt(request.getParameter("days"));
            String reason = request.getParameter("reason");

            User user = User.loadFromDB(userId);

            LeaveService service = new LeaveService();
            OperationResult result =
                service.applyLeave(user, days, reason, leaveType, leaveDate);


            request.setAttribute("message", result.getMessage());
            request.setAttribute("messageType",
                    result.isSuccess() ? "success" : "error");

        } catch (Exception e) {
            request.setAttribute("message", "Invalid input");
            request.setAttribute("messageType", "error");
        }
    }

    private void viewBalance(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            int userId = (int) session.getAttribute("userId");

            LeaveService service = new LeaveService();
            int remaining = service.getRemainingLeaves(userId);

            request.setAttribute("balance", remaining);

        } catch (Exception e) {
            request.setAttribute("message", "Invalid user id");
            request.setAttribute("messageType", "error");
        }
    }
}
