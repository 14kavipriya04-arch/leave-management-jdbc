package com.leavemanagement.servlet;

// import com.leavemanagement.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int userId = Integer.parseInt(req.getParameter("userId"));
        String password = req.getParameter("password");

        User user = User.login(userId, password);

        if (user == null) {
            req.setAttribute("message", "Invalid User ID or Password");
            req.getRequestDispatcher("/index.jsp").forward(req, res);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("role", user.getRole());

        if ("MANAGER".equalsIgnoreCase(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/manager");
        } else {
            res.sendRedirect(req.getContextPath() + "/employee");
        }
    }
        @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("role") != null) {

            String role = session.getAttribute("role").toString();

            if ("MANAGER".equalsIgnoreCase(role)) {
                res.sendRedirect(req.getContextPath() + "/manager");
            } else {
                res.sendRedirect(req.getContextPath() + "/employee");
            }
            return;
        }

        try {
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendError(500);
        }
    }

}
