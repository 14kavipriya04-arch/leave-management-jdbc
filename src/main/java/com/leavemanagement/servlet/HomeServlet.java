package com.leavemanagement.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));

        User user = User.loadFromDB(userId);

        if (user == null) {
            request.setAttribute("message", "Invalid user");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // ✅ Create session only after valid login
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("role", user.getRole()); 

        //  Redirect based on REAL role
        if ("Manager".equalsIgnoreCase(user.getRole().trim())) {
            response.sendRedirect(request.getContextPath() + "/manager");
        } else {
            response.sendRedirect(request.getContextPath() + "/employee");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Login page → load users
        List<User> users = User.getAllEmployees();
        request.setAttribute("users", users);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
