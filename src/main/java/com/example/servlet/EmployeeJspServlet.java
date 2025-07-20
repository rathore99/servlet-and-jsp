package com.example.servlet;

import com.example.model.Employee;
import com.example.model.SimpleEmployee;
import com.example.service.EmployeeService;
import com.example.service.EmployeeServiceImpl;
import com.example.service.SimpleEmployeeService;
import com.example.dao.EmployeeAuthDAO;
import com.example.utils.PasswordUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "EmployeeJspServlet", urlPatterns = {"/employees-jsp/*"})
@MultipartConfig(fileSizeThreshold=1024*1024*1, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*10)
public class EmployeeJspServlet extends HttpServlet {
    private final SimpleEmployeeService service = new SimpleEmployeeService();
    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");
        if (action == null) {
            List<SimpleEmployee> all = service.getAll();
            req.setAttribute("employees", all);
            req.getRequestDispatcher("/WEB-INF/view/index.jsp").forward(req, resp);
        } else if (action.equals("add")) {
            req.getRequestDispatcher("/WEB-INF/view/add.jsp").forward(req, resp);
        } else if (action.equals("update")) {
            req.getRequestDispatcher("/WEB-INF/view/update.jsp").forward(req, resp);
        } else if (action.equals("delete")) {
            req.getRequestDispatcher("/WEB-INF/view/delete.jsp").forward(req, resp);
        } else if (action.equals("search")) {
            if (idParam == null) {
                req.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(req, resp);
            } else {
                try {
                    int id = Integer.parseInt(idParam);
                    SimpleEmployee emp = service.get(id);
                    req.setAttribute("employee", emp);
                } catch (NumberFormatException e) {
                    req.setAttribute("invalidId", true);
                }
                req.getRequestDispatcher("/WEB-INF/view/searchResult.jsp").forward(req, resp);
            }
        } else if (action.equals("addPhoto")) {
            req.getRequestDispatcher("/WEB-INF/view/addPhoto.jsp").forward(req, resp);
        } else if (action.equals("updatePhoto")) {
            req.getRequestDispatcher("/WEB-INF/view/updatePhoto.jsp").forward(req, resp);
        } else if (action.equals("removePhoto")) {
            req.getRequestDispatcher("/WEB-INF/view/removePhoto.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        String photoAction = req.getParameter("photoAction");
        if (photoAction != null) {
            if (photoAction.equals("add")) {
                handleAddPhoto(req, resp);
                return;
            } else if (photoAction.equals("update")) {
                handleUpdatePhoto(req, resp);
                return;
            } else if (photoAction.equals("remove")) {
                handleRemovePhoto(req, resp);
                return;
            }
        }
        if (method == null) {
            // Add
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            SimpleEmployee emp = new SimpleEmployee();
            emp.setName(name);
            emp.setDepartment(department);
            SimpleEmployee created = service.create(emp);
            // Generate password: first 4 letters of name + current date ddMMyyyy
            String first4 = name.length() >= 4 ? name.substring(0, 4) : name;
            String dateStr = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String rawPassword = first4 + dateStr;
            // Register in EmployeeAuthDAO (use name as email/username for demo)
            Employee employee = Employee.fromName(created.getName());
            //employee.setId(created.getId());
            employee = employeeService.create(employee);
            EmployeeAuthDAO authDAO = new EmployeeAuthDAO();
            authDAO.registerEmployee(employee.getId(), name, rawPassword);
            req.setAttribute("employee", created);
            req.setAttribute("passwordMessage", "Your password is: " + rawPassword + " (first 4 letters of name + today's date ddMMyyyy)");
            req.getRequestDispatcher("/WEB-INF/view/addResult.jsp").forward(req, resp);
        } else if (method.equalsIgnoreCase("put")) {
            // Update
            String idStr = req.getParameter("id");
            String name = req.getParameter("name");
            String department = req.getParameter("department");
            SimpleEmployee updated = null;
            try {
                int id = Integer.parseInt(idStr);
                SimpleEmployee emp = new SimpleEmployee();
                emp.setName(name);
                emp.setDepartment(department);
                updated = service.update(id, emp);
            } catch (NumberFormatException e) {
                req.setAttribute("invalidId", true);
            }
            req.setAttribute("employee", updated);
            req.getRequestDispatcher("/WEB-INF/view/updateResult.jsp").forward(req, resp);
        } else if (method.equalsIgnoreCase("delete")) {
            // Delete
            String idStr = req.getParameter("id");
            boolean deleted = false;
            try {
                int id = Integer.parseInt(idStr);
                deleted = service.delete(id);
                req.setAttribute("deletedId", id);
            } catch (NumberFormatException e) {
                req.setAttribute("invalidId", true);
            }
            req.setAttribute("deleted", deleted);
            req.getRequestDispatcher("/WEB-INF/view/deleteResult.jsp").forward(req, resp);
        }
    }

    private void handleAddPhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            SimpleEmployee emp = service.get(id);
            if (emp == null) {
                req.setAttribute("invalidId", true);
                req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
                return;
            }
            Part filePart = req.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                String uploadPath = getServletContext().getRealPath("/images/") + fileName;
                filePart.write(uploadPath);
                emp.setProfilePhoto("images/" + fileName);
                service.update(id, emp);
                req.setAttribute("employee", emp);
                req.setAttribute("photoAction", "add");
            }
        } catch (Exception e) {
            req.setAttribute("invalidId", true);
        }
        req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
    }

    private void handleUpdatePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            SimpleEmployee emp = service.get(id);
            if (emp == null) {
                req.setAttribute("invalidId", true);
                req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
                return;
            }
            Part filePart = req.getPart("photo");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                String uploadPath = getServletContext().getRealPath("/images/") + fileName;
                filePart.write(uploadPath);
                emp.setProfilePhoto("images/" + fileName);
                service.update(id, emp);
                req.setAttribute("employee", emp);
                req.setAttribute("photoAction", "update");
            }
        } catch (Exception e) {
            req.setAttribute("invalidId", true);
        }
        req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
    }

    private void handleRemovePhoto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            SimpleEmployee emp = service.get(id);
            if (emp == null) {
                req.setAttribute("invalidId", true);
                req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
                return;
            }
            emp.setProfilePhoto(null);
            service.update(id, emp);
            req.setAttribute("employee", emp);
            req.setAttribute("photoAction", "remove");
        } catch (Exception e) {
            req.setAttribute("invalidId", true);
        }
        req.getRequestDispatcher("/WEB-INF/view/photoResult.jsp").forward(req, resp);
    }
} 