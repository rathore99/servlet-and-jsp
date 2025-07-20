package com.example.servlet;

import com.example.model.SimpleEmployee;
import com.example.service.SimpleEmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EmployeeHttpServlet", urlPatterns = {"/employees-html/*"})
public class EmployeeHttpServlet extends HttpServlet {
    private final SimpleEmployeeService service = new SimpleEmployeeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Employees</title></head><body style='max-width:600px;margin:auto;'>");
        html.append("<h1>Employee Management (HTML)</h1>");
        if (action == null) {
            // Home page: show action buttons
            html.append("<div style='display:flex;flex-direction:column;gap:1em;margin:2em 0;'>");
            html.append("<form method='get' action='/employees-html/'><button type='submit' name='action' value='add' style='width:100%'>Add Employee</button></form>");
            html.append("<form method='get' action='/employees-html/'><button type='submit' name='action' value='update' style='width:100%'>Update Employee</button></form>");
            html.append("<form method='get' action='/employees-html/'><button type='submit' name='action' value='delete' style='width:100%'>Delete Employee</button></form>");
            html.append("<form method='get' action='/employees-html/'><button type='submit' name='action' value='search' style='width:100%'>Search Employee</button></form>");
            html.append("</div>");
            html.append("<hr><h2>All Employees</h2><ul>");
            for (SimpleEmployee emp : service.getAll()) {
                html.append("<li>")
                    .append(emp.getId()).append(": ")
                    .append(emp.getName()).append(" (" + emp.getDepartment() + ")")
                    .append("</li>");
            }
            html.append("</ul>");
        } else if (action.equals("add")) {
            html.append("<h2>Add Employee</h2>")
                .append("<form method='post' action='/employees-html/' style='margin-bottom:2em;'>")
                .append("<label>Name:<br><input name='name' required style='width:100%'></label><br><br>")
                .append("<label>Department:<br><input name='department' required style='width:100%'></label><br><br>")
                .append("<button type='submit' style='width:100%'>Add Employee</button>")
                .append("</form>");
            html.append(backHomeButton());
        } else if (action.equals("update")) {
            html.append("<h2>Update Employee</h2>")
                .append("<form method='post' action='/employees-html/' style='margin-bottom:2em;'>")
                .append("<input type='hidden' name='_method' value='put'>")
                .append("<label>ID:<br><input name='id' required style='width:100%'></label><br><br>")
                .append("<label>Name:<br><input name='name' required style='width:100%'></label><br><br>")
                .append("<label>Department:<br><input name='department' required style='width:100%'></label><br><br>")
                .append("<button type='submit' style='width:100%'>Update Employee</button>")
                .append("</form>");
            html.append(backHomeButton());
        } else if (action.equals("delete")) {
            html.append("<h2>Delete Employee</h2>")
                .append("<form method='post' action='/employees-html/' style='margin-bottom:2em;'>")
                .append("<input type='hidden' name='_method' value='delete'>")
                .append("<label>ID:<br><input name='id' required style='width:100%'></label><br><br>")
                .append("<button type='submit' style='width:100%'>Delete Employee</button>")
                .append("</form>");
            html.append(backHomeButton());
        } else if (action.equals("search")) {
            if (idParam == null) {
                html.append("<h2>Search Employee</h2>")
                    .append("<form method='get' action='/employees-html/' style='margin-bottom:2em;'>")
                    .append("<input type='hidden' name='action' value='search'>")
                    .append("<label>ID:<br><input name='id' required style='width:100%'></label><br><br>")
                    .append("<button type='submit' style='width:100%'>Search</button>")
                    .append("</form>");
                html.append(backHomeButton());
            } else {
                try {
                    int id = Integer.parseInt(idParam);
                    SimpleEmployee emp = service.get(id);
                    if (emp == null) {
                        html.append("<h2>Employee Not Found</h2>");
                    } else {
                        html.append("<h2>Employee Details</h2>")
                            .append("<p>ID: ").append(emp.getId()).append("<br>")
                            .append("Name: ").append(emp.getName()).append("<br>")
                            .append("Department: ").append(emp.getDepartment()).append("</p>");
                    }
                } catch (NumberFormatException e) {
                    html.append("<h2>Invalid ID</h2>");
                }
                html.append(backHomeButton());
            }
        }
        html.append("</body></html>");
        resp.getWriter().write(html.toString());
    }

    private String backHomeButton() {
        return "<form method='get' action='/employees-html/' style='margin-top:2em;'><button type='submit' style='width:100%'>Back to Home</button></form>";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if (method == null) {
            // Handle as create (POST)
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            SimpleEmployee emp = new SimpleEmployee();
            emp.setName(name);
            emp.setDepartment(department);
            SimpleEmployee created = service.create(emp);
            resp.setContentType("text/html;charset=UTF-8");
            StringBuilder html = new StringBuilder();
            html.append("<html><body style='max-width:600px;margin:auto;'>");
            html.append("<h2>Employee Added</h2>");
            html.append("<p>ID: ").append(created.getId()).append("<br>");
            html.append("Name: ").append(created.getName()).append("<br>");
            html.append("Department: ").append(created.getDepartment()).append("</p>");
            html.append(backHomeButton());
            html.append("</body></html>");
            resp.getWriter().write(html.toString());
        } else if (method.equalsIgnoreCase("put")) {
            doPut(req, resp);
        } else if (method.equalsIgnoreCase("delete")) {
            doDelete(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String department = req.getParameter("department");
        StringBuilder html = new StringBuilder();
        resp.setContentType("text/html;charset=UTF-8");
        html.append("<html><body style='max-width:600px;margin:auto;'>");
        try {
            int id = Integer.parseInt(idStr);
            SimpleEmployee emp = new SimpleEmployee();
            emp.setName(name);
            emp.setDepartment(department);
            SimpleEmployee updated = service.update(id, emp);
            if (updated == null) {
                html.append("<h2>Employee Not Found</h2>");
            } else {
                html.append("<h2>Employee Updated</h2>");
                html.append("<p>ID: ").append(updated.getId()).append("<br>");
                html.append("Name: ").append(updated.getName()).append("<br>");
                html.append("Department: ").append(updated.getDepartment()).append("</p>");
            }
        } catch (NumberFormatException e) {
            html.append("<h2>Invalid ID</h2>");
        }
        html.append(backHomeButton());
        html.append("</body></html>");
        resp.getWriter().write(html.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        StringBuilder html = new StringBuilder();
        resp.setContentType("text/html;charset=UTF-8");
        html.append("<html><body style='max-width:600px;margin:auto;'>");
        try {
            int id = Integer.parseInt(idStr);
            boolean deleted = service.delete(id);
            if (!deleted) {
                html.append("<h2>Employee Not Found</h2>");
            } else {
                html.append("<h2>Employee Deleted</h2>");
                html.append("<p>Employee with ID ").append(id).append(" has been deleted.</p>");
            }
        } catch (NumberFormatException e) {
            html.append("<h2>Invalid ID</h2>");
        }
        html.append(backHomeButton());
        html.append("</body></html>");
        resp.getWriter().write(html.toString());
    }
} 