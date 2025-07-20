package com.example.dao;

import com.example.model.Department;
import com.example.model.Employee;
import com.example.utils.ConnectionPool;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import com.example.utils.AppLogger;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EmployeeDAO {
    // private static final Logger log = LogManager.getLogger(EmployeeDAO.class); // log4j 2.x
    private static final AppLogger logger = AppLogger.getInstance(); // Using custom logger from utils
    // Add a new employee
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public boolean addManager(Employee emp){
        logger.info("Adding employee: " + emp.getEmail());
        String sql = "INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        boolean result =false;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, emp.getId());
                ps.setString(2, emp.getFirstName());
                ps.setString(3, emp.getLastName());
                ps.setString(4, emp.getEmail());
                ps.setString(5, emp.getMobileNumber());
                ps.setDate(6, new java.sql.Date(emp.getJoiningDate().getTime()));
                ps.setString(7, emp.getJobId());
                ps.setDouble(8, emp.getSalary());
                ps.setInt(9, emp.getDepartment() != null ? emp.getDepartment().getDepartmentId() : 0);
                result = ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            logger.error("Error adding employee: " + e.getMessage());
            //log.error("Error adding employee: " + e.getMessage(), e\\);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return result;
    }
    public boolean addEmployee(Employee emp) {
        // Logger logger = Logger.getInstance();
        // logger.info("Adding employee: " + emp.getEmail());
        logger.info("Adding employee: " + emp.getEmail());
        String sql = "INSERT INTO employees (employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, department_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, emp.getId());
                ps.setString(2, emp.getFirstName());
                ps.setString(3, emp.getLastName());
                ps.setString(4, emp.getEmail());
                ps.setString(5, emp.getMobileNumber());
                ps.setDate(6, new java.sql.Date(emp.getJoiningDate().getTime()));
                ps.setString(7, emp.getJobId());
                ps.setDouble(8, emp.getSalary());
                ps.setInt(9, emp.getDepartment() != null ? emp.getDepartment().getDepartmentId() : 0);
                boolean result = ps.executeUpdate() > 0;
                if(departmentDAO.getDepartmentById(emp.getDepartment().getDepartmentId()) == null){
                    Employee manager = Employee.fromName(emp.getDepartment().getManagerName());
                    Random random = new Random();
                    manager.setId(random.nextInt(100000));
                    addManager(manager);
                    departmentDAO.addDepartment(emp.getDepartment(),manager.getId());
                }

                // logger.info("Add employee result: " + result);
                logger.info("Add employee result: " + result);
                return result;
            }
        } catch (Exception e) {
             logger.error("Error adding employee: " + e.getMessage());
            //log.error("Error adding employee: " + e.getMessage(), e\\);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Get employee by ID (with department name)
    public Employee getEmployeeById(int id) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Fetching employee by ID: " + id);
        logger.debug("Fetching employee by ID: " + id);
        String sql = "SELECT e.*, d.department_id, d.department_name, d.manager_id FROM employees e JOIN departments d ON e.department_id = d.department_id WHERE e.employee_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Employee emp = new Employee();
                        emp.setId(rs.getInt("employee_id"));
                        emp.setFirstName(rs.getString("first_name"));
                        emp.setLastName(rs.getString("last_name"));
                        emp.setEmail(rs.getString("email"));
                        emp.setMobileNumber(rs.getString("phone_number"));
                        emp.setJoiningDate(rs.getDate("hire_date"));
                        emp.setJobId(rs.getString("job_id"));
                        emp.setSalary(rs.getDouble("salary"));
                        Department dept = new Department();
                        dept.setDepartmentId(rs.getInt("department_id"));
                        dept.setDepartmentName(rs.getString("department_name"));
                        dept.setManagerName(null); // managerName can be set with another query if needed
                        emp.setDepartment(dept);
                        // logger.info("Employee found: " + emp.getEmail());
                        logger.info("Employee found: " + emp.getEmail());
                        return emp;
                    }
                }
            }
        } catch (Exception e) {
             logger.error("Error fetching employee by ID: " + e.getMessage());
            //log.error("Error fetching employee by ID: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        // logger.warn("No employee found for ID: " + id);
        logger.warn("No employee found for ID: " + id);
        return null;
    }

    // Update employee
    public boolean updateEmployee(Employee emp) {
        // Logger logger = Logger.getInstance();
        // logger.info("Updating employee: " + emp.getEmail());
        logger.info("Updating employee: " + emp.getEmail());
        String sql = "UPDATE employees SET first_name=?, last_name=?, email=?, phone_number=?, hire_date=?, job_id=?, salary=?, department_id=? WHERE employee_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, emp.getFirstName());
                ps.setString(2, emp.getLastName());
                ps.setString(3, emp.getEmail());
                ps.setString(4, emp.getMobileNumber());
                ps.setDate(5, new java.sql.Date(emp.getJoiningDate().getTime()));
                ps.setString(6, emp.getJobId());
                ps.setDouble(7, emp.getSalary());
                ps.setInt(8, emp.getDepartment() != null ? emp.getDepartment().getDepartmentId() : 0);
                ps.setInt(9, emp.getId());
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Update employee result: " + result);
                logger.info("Update employee result: " + result);
                return result;
            }
        } catch (Exception e) {
             logger.error("Error updating employee: " + e.getMessage());
            //log.error("Error updating employee: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Delete employee
    public boolean deleteEmployee(int id) {
        // Logger logger = Logger.getInstance();
        // logger.info("Deleting employee ID: " + id);
        logger.info("Deleting employee ID: " + id);
        String sql = "DELETE FROM employees WHERE employee_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Delete employee result: " + result);
                logger.info("Delete employee result: " + result);
                return result;
            }
        } catch (Exception e) {
             logger.error("Error deleting employee: " + e.getMessage());
            //log.error("Error deleting employee: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Get all employees (with department name)
    public List<Employee> getAllEmployees() {
        // Logger logger = Logger.getInstance();
        // logger.debug("Fetching all employees");
        logger.debug("Fetching all employees");
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.department_id, d.department_name, d.manager_id FROM employees e JOIN departments d ON e.department_id = d.department_id";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee emp = new Employee();
                    emp.setId(rs.getInt("employee_id"));
                    emp.setFirstName(rs.getString("first_name"));
                    emp.setLastName(rs.getString("last_name"));
                    emp.setEmail(rs.getString("email"));
                    emp.setMobileNumber(rs.getString("phone_number"));
                    emp.setJoiningDate(rs.getDate("hire_date"));
                    emp.setJobId(rs.getString("job_id"));
                    emp.setSalary(rs.getDouble("salary"));
                    Department dept = new Department();
                    dept.setDepartmentId(rs.getInt("department_id"));
                    dept.setDepartmentName(rs.getString("department_name"));
                    dept.setManagerName(null);
                    emp.setDepartment(dept);
                    list.add(emp);
                }
                // logger.info("Total employees fetched: " + list.size());
                logger.info("Total employees fetched: " + list.size());
            }
        } catch (Exception e) {
             logger.error("Error fetching all employees: " + e.getMessage());
            //logger.error("Error fetching all employees: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    // Search employees by name, email, or department name
    public List<Employee> searchEmployees(String keyword) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Searching employees with keyword: " + keyword);
        logger.debug("Searching employees with keyword: " + keyword);
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, d.department_id, d.department_name, d.manager_id FROM employees e JOIN departments d ON e.department_id = d.department_id WHERE e.first_name LIKE ? OR e.last_name LIKE ? OR e.email LIKE ? OR d.department_name LIKE ?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String like = "%" + keyword + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Employee emp = new Employee();
                        emp.setId(rs.getInt("employee_id"));
                        emp.setFirstName(rs.getString("first_name"));
                        emp.setLastName(rs.getString("last_name"));
                        emp.setEmail(rs.getString("email"));
                        emp.setMobileNumber(rs.getString("phone_number"));
                        emp.setJoiningDate(rs.getDate("hire_date"));
                        emp.setJobId(rs.getString("job_id"));
                        emp.setSalary(rs.getDouble("salary"));
                        Department dept = new Department();
                        dept.setDepartmentId(rs.getInt("department_id"));
                        dept.setDepartmentName(rs.getString("department_name"));
                        dept.setManagerName(null);
                        emp.setDepartment(dept);
                        list.add(emp);
                    }
                    // logger.info("Total search results: " + list.size());
                    logger.info("Total search results: " + list.size());
                }
            }
        } catch (Exception e) {
             logger.error("Error searching employees: " + e.getMessage());
            //logger.error("Error searching employees: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    // Get Department by ID (with manager name)
    public Department getDepartmentById(int departmentId) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Fetching department by ID: " + departmentId);
        logger.debug("Fetching department by ID: " + departmentId);
        String sql = "SELECT d.*, e.first_name, e.last_name FROM departments d LEFT JOIN employees e ON d.manager_id = e.employee_id WHERE d.department_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, departmentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String managerName = rs.getString("first_name") != null ? rs.getString("first_name") + " " + rs.getString("last_name") : null;
                        // logger.info("Department found: " + rs.getString("department_name"));
                        logger.info("Department found: " + rs.getString("department_name"));
                        return new Department(
                                rs.getInt("department_id"),
                                rs.getString("department_name"),
                                managerName
                        );
                    }
                }
            }
        } catch (Exception e) {
             logger.error("Error fetching department by ID: " + e.getMessage());
            //logger.error("Error fetching department by ID: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        // logger.warn("No department found for ID: " + departmentId);
        logger.warn("No department found for ID: " + departmentId);
        return null;
    }

    // Check if a user is the manager of an employee
    public boolean isManagerOf(int managerId, int employeeId) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Checking if user " + managerId + " is manager of employee " + employeeId);
        logger.debug("Checking if user " + managerId + " is manager of employee " + employeeId);
        String sql = "SELECT 1 FROM employees e JOIN departments d ON e.department_id = d.department_id WHERE e.employee_id = ? AND d.manager_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setInt(2, managerId);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean result = rs.next();
                    // logger.info("Is manager: " + result);
                    logger.info("Is manager: " + result);
                    return result;
                }
            }
        } catch (Exception e) {
             logger.error("Error checking manager relationship: " + e.getMessage());
            //logger.error("Error checking manager relationship: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return false;
    }
}