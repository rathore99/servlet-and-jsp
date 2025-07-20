package com.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "SessionCookieDemoServlet", urlPatterns = {"/session-cookie-demo"})
public class SessionCookieDemoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = (String) session.getAttribute("username");
        String action = req.getParameter("action");
        String message = null;
        // Counter logic
        int counter = 0;
        int requestCount = 0;
        // Read cookies
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("counter")) {
                    try { counter = Integer.parseInt(c.getValue()); } catch (Exception ignored) {}
                }
                if (c.getName().equals("requestCount")) {
                    try { requestCount = Integer.parseInt(c.getValue()); } catch (Exception ignored) {}
                }
            }
        }
        requestCount++;
        if (requestCount % 5 == 0) {
            counter++;
            Cookie counterCookie = new Cookie("counter", String.valueOf(counter));
            counterCookie.setMaxAge(60 * 60 * 24 * 7);
            counterCookie.setPath("/");
            resp.addCookie(counterCookie);
        }
        // Always update requestCount cookie
        Cookie requestCountCookie = new Cookie("requestCount", String.valueOf(requestCount));
        requestCountCookie.setMaxAge(60 * 60 * 24 * 7);
        requestCountCookie.setPath("/");
        resp.addCookie(requestCountCookie);
        if (action != null && action.equals("logout")) {
            session.invalidate();
            Cookie userCookie = new Cookie("user", "");
            userCookie.setMaxAge(0);
            userCookie.setPath("/");
            resp.addCookie(userCookie);
            // Remove counter and requestCount cookies
            Cookie counterCookie = new Cookie("counter", "");
            counterCookie.setMaxAge(0);
            counterCookie.setPath("/");
            resp.addCookie(counterCookie);
            Cookie requestCountCookie2 = new Cookie("requestCount", "");
            requestCountCookie2.setMaxAge(0);
            requestCountCookie2.setPath("/");
            resp.addCookie(requestCountCookie2);
            message = "You have been logged out.";
        } else if (username == null) {
            // Check for user cookie
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("user") && c.getValue() != null && !c.getValue().isEmpty()) {
                        username = c.getValue();
                        session.setAttribute("username", username);
                        break;
                    }
                }
            }
        }
        req.setAttribute("username", username);
        req.setAttribute("message", message);
        req.setAttribute("counter", counter);
        req.getRequestDispatcher("/WEB-INF/view/sessionCookieDemo.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if (username != null && !username.trim().isEmpty()) {
            // Simulate login: set session and cookie
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            Cookie userCookie = new Cookie("user", username);
            userCookie.setMaxAge(60 * 60 * 24 * 7); // 1 week
            resp.addCookie(userCookie);
            req.setAttribute("username", username);
            req.setAttribute("message", "Login successful. Session and cookie set.");
        } else {
            req.setAttribute("message", "Please enter a username.");
        }
        req.getRequestDispatcher("/WEB-INF/view/sessionCookieDemo.jsp").forward(req, resp);
    }
} 