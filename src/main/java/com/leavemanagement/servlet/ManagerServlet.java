package com.leavemanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                
        HttpSession session = request.getSession(false);
        if (session == null ||
            session.getAttribute("role") == null ||
            !"MANAGER".equalsIgnoreCase(session.getAttribute("role").toString())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        viewPendingLeaves(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

                
                HttpSession session = request.getSession(false);
                
                if (session == null ||
                    session.getAttribute("role") == null ||
                    !"MANAGER".equalsIgnoreCase(session.getAttribute("role").toString())) {
                        
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                        return;
                    }
        String action = request.getParameter("action");
        if ("approve".equalsIgnoreCase(action)) {
                        approveLeave(request);
        } else if ("deny".equalsIgnoreCase(action)) {
            denyLeave(request);
        }

        viewPendingLeaves(request, response);
    }

    private void viewPendingLeaves(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

         HttpSession session = request.getSession(false);

        
        LeaveService service = new LeaveService();
        List<LeaveService.LeaveRequestData> pending =
                service.getPendingLeaves();

        request.setAttribute("pendingRequests", pending);
        request.getRequestDispatcher("/manager.jsp")
               .forward(request, response);
    }

    private void approveLeave(HttpServletRequest request) {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
    
            HttpSession session = request.getSession(false);
            String managerName = (String) session.getAttribute("userName");

            LeaveService service = new LeaveService();
            OperationResult result = service.approveLeave(requestId, managerName);

            request.setAttribute("message", result.getMessage());
            request.setAttribute("messageType",
                    result.isSuccess() ? "success" : "error");

        } catch (Exception e) {
            request.setAttribute("message", "Approval failed");
            request.setAttribute("messageType", "error");
        }
    }

    private void denyLeave(HttpServletRequest request) {
        try {
            int requestId = Integer.parseInt(request.getParameter("requestId"));
            String comment = request.getParameter("comment");

            LeaveService service = new LeaveService();
            OperationResult result =
                    service.declineLeave(requestId, comment);

            request.setAttribute("message", result.getMessage());
            request.setAttribute("messageType",
                    result.isSuccess() ? "success" : "error");

        } catch (Exception e) {
            request.setAttribute("message", "Denial failed");
            request.setAttribute("messageType", "error");
        }
    }
}
