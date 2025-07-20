package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String username = null;
        if (session != null) {
            username = (String) session.getAttribute("authUser");
        }
        if (username == null) {
            // Check cookie
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("authUser") && c.getValue() != null && !c.getValue().isEmpty()) {
                        username = c.getValue();
                        // Restore session
                        session = req.getSession();
                        session.setAttribute("authUser", username);
                        break;
                    }
                }
            }
        }
        if (username == null) {
            resp.sendRedirect("/auth");
            return;
        }
        req.setAttribute("username", username);
        req.getRequestDispatcher("/WEB-INF/view/dashboard.jsp").forward(req, resp);
    }
} 