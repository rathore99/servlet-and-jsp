package com.example.servlet;

import com.example.dao.EmployeeAuthDAO;
import com.example.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        if ("logout".equals(action)) {
            if (session != null) session.invalidate();
            Cookie userCookie = new Cookie("authUser", "");
            userCookie.setMaxAge(0);
            userCookie.setPath("/");
            resp.addCookie(userCookie);
            req.setAttribute("message", "You have been logged out.");
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        } else if (session != null && session.getAttribute("authUser") != null) {
            resp.sendRedirect("/dashboard");
        } else {
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        // For demo: hardcoded user/pass. In real apps, check DB or LDAP.
        if ("admin".equals(username) && "admin123".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("authUser", username);
            Cookie userCookie = new Cookie("authUser", username);
            userCookie.setMaxAge(60 * 60 * 24 * 7);
            userCookie.setPath("/");
            resp.addCookie(userCookie);
            resp.sendRedirect("/dashboard");
            return;
        }
        // Employee login via DB
        EmployeeAuthDAO authDAO = new EmployeeAuthDAO();
        Integer empId = authDAO.authenticate(username, password);
        if (empId != null) {
            HttpSession session = req.getSession();
            session.setAttribute("authUser", username);
            session.setAttribute("employeeId", empId);
            Cookie userCookie = new Cookie("authUser", username);
            userCookie.setMaxAge(60 * 60 * 24 * 7);
            userCookie.setPath("/");
            resp.addCookie(userCookie);
            resp.sendRedirect("/dashboard");
        } else {
            req.setAttribute("message", "Invalid username or password.");
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
        }
    }
} 