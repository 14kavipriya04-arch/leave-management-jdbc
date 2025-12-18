package com.leavemanagement.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter({"/manager", "/employee"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        // Not logged in
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        String role = session.getAttribute("role").toString();
        String path = request.getServletPath();

        // Employee trying manager URL
        if (path.equals("/manager") && !"MANAGER".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Manager trying employee URL
        if (path.equals("/employee") && !"EMPLOYEE".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        chain.doFilter(req, res);
    }
}
