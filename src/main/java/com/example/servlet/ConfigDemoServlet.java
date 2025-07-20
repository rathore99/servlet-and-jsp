package com.example.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import jakarta.servlet.http.Cookie;

@WebServlet(name = "ConfigDemoServlet", urlPatterns = {"/config-demo"}, loadOnStartup = 1)
public class ConfigDemoServlet extends HttpServlet {
    private String appWideSetting;
    private String servletSpecificSetting;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // Example: Read servlet-specific init param
        servletSpecificSetting = config.getInitParameter("servletSetting");
        // Example: Read application-wide context param
        ServletContext context = config.getServletContext();
        appWideSetting = context.getInitParameter("appSetting");
        // Example: Store something in context for sharing
        context.setAttribute("startupTime", System.currentTimeMillis());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        req.setAttribute("servletSpecificSetting", servletSpecificSetting);
        req.setAttribute("appWideSetting", appWideSetting);
        req.setAttribute("startupTime", context.getAttribute("startupTime"));
        req.setAttribute("contextPath", context.getContextPath());
        req.setAttribute("imagesRealPath", context.getRealPath("/images"));
        req.setAttribute("servletName", getServletConfig().getServletName());
        req.setAttribute("initParams", getServletConfig().getInitParameterNames());
        req.setAttribute("servletConfig", getServletConfig());
        req.getRequestDispatcher("/WEB-INF/view/configDemo.jsp").forward(req, resp);
    }

    @Override
    public void destroy() {
        // Example: Cleanup resources, log shutdown, etc.
        ServletContext context = getServletContext();
        context.log("ConfigDemoServlet is being destroyed. Cleaning up resources...");
    }
} 