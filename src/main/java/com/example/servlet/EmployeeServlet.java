package com.example.servlet;

import com.example.model.Employee;
import com.example.service.EmployeeService;
import com.example.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dao.EmployeeAuthDAO;
import com.example.utils.PasswordUtils;
import java.text.SimpleDateFormat;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employees/*"})
public class EmployeeServlet extends HttpServlet {
    private final EmployeeService service = new EmployeeServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Employee> all = service.getAll();
            mapper.writeValue(resp.getOutputStream(), all);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Employee emp = service.get(id);
                if (emp == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{}\n");
                } else {
                    mapper.writeValue(resp.getOutputStream(), emp);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Invalid ID\"}\n");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Employee emp = mapper.readValue(req.getInputStream(), Employee.class);
        Employee created = service.create(emp);
        // Generate password: first 4 letters of first name + joining date ddMMyyyy
        String first4 = emp.getFirstName() != null && emp.getFirstName().length() >= 4 ? emp.getFirstName().substring(0, 4) : emp.getFirstName();
        String dateStr = emp.getJoiningDate() != null ? new SimpleDateFormat("ddMMyyyy").format(emp.getJoiningDate()) : new SimpleDateFormat("ddMMyyyy").format(new java.util.Date());
        String rawPassword = first4 + dateStr;
        // Register in EmployeeAuthDAO (use email as username)
        EmployeeAuthDAO authDAO = new EmployeeAuthDAO();
        authDAO.registerEmployee(created.getId(), created.getEmail(), rawPassword);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        // Add password message to JSON response
        java.util.Map<String, Object> responseMap = new java.util.HashMap<>();
        responseMap.put("employee", created);
        responseMap.put("passwordMessage", "Your password is: " + rawPassword + " (first 4 letters of first name + joining date ddMMyyyy)");
        mapper.writeValue(resp.getOutputStream(), responseMap);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID required\"}\n");
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Employee emp = mapper.readValue(req.getInputStream(), Employee.class);
            Employee updated = service.update(id, emp);
            if (updated == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{}\n");
            } else {
                mapper.writeValue(resp.getOutputStream(), updated);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid ID\"}\n");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"ID required\"}\n");
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = service.delete(id);
            if (!deleted) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{}\n");
            } else {
                resp.getWriter().write("{\"status\":\"deleted\"}\n");
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\":\"Invalid ID\"}\n");
        }
    }

    // HEAD method: Like GET but returns only headers, no body. Used to check what a GET would return (metadata, content-type, etc.)
    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        if (pathInfo == null || pathInfo.equals("/")) {
            // Simulate GET all employees, but do not write body
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Employee emp = service.get(id);
                if (emp == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        // No body is written for HEAD
    }

    // OPTIONS method: Tells client what HTTP methods are supported for this endpoint
    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Allow: GET, POST, PUT, DELETE, HEAD, OPTIONS
        resp.setHeader("Allow", "GET,POST,PUT,DELETE,HEAD,OPTIONS");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
    }
} 