package com.leavemanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        int userId;
        String password;

        try {
            userId = Integer.parseInt(req.getParameter("userId"));
            password = req.getParameter("password");
        } catch (Exception e) {
            req.setAttribute("message", "Invalid input");
            req.getRequestDispatcher("/index.jsp").forward(req, res);
            return;
        }

        User user = User.login(userId, password);

        if (user == null) {
            req.setAttribute("message", "Invalid User ID or Password");
            req.getRequestDispatcher("/index.jsp").forward(req, res);
            return;
        }

        HttpSession session = req.getSession(true);
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
            throws IOException, ServletException {

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

        req.getRequestDispatcher("/index.jsp").forward(req, res);
    }
}
