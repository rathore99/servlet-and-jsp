package com.example.dao;

import com.example.model.Department;
import com.example.utils.ConnectionPool;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;
import com.example.utils.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    // private static final Logger log = LogManager.getLogger(DepartmentDAO.class); // log4j 2.x
    private static final AppLogger log = AppLogger.getInstance(); // Using custom logger from utils
    // Add a new department
    public boolean addDepartment(Department dept, int managerId) {
        log.info("Adding department: " + dept.getDepartmentName());
        String sql = "INSERT INTO departments (department_id, department_name, manager_id) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dept.getDepartmentId());
            ps.setString(2, dept.getDepartmentName());
            ps.setInt(3, managerId);
            boolean result = ps.executeUpdate() > 0;
            log.info("Add department result: " + result);
            ConnectionPool.getInstance().releaseConnection(conn);
            return result;
        } catch (Exception e) {
            log.error("Error adding department: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get department by ID (with manager name)
    public Department getDepartmentById(int departmentId) {
        log.debug("Fetching department by ID: " + departmentId);
        String sql = "SELECT d.*, e.first_name, e.last_name FROM departments d LEFT JOIN employees e ON d.manager_id = e.employee_id WHERE d.department_id = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Department found: " + rs.getString("department_name"));
                    String managerName = rs.getString("first_name") != null ? rs.getString("first_name") + " " + rs.getString("last_name") : null;
                    return new Department(
                            rs.getInt("department_id"),
                            rs.getString("department_name"),
                            managerName
                    );
                }
            }
        } catch (Exception e) {
            log.error("Error fetching department by ID: " + e.getMessage());
            e.printStackTrace();
        }
        log.warn("No department found for ID: " + departmentId);
        return null;
    }

    // Update department
    public boolean updateDepartment(Department dept, int managerId) {
        log.info("Updating department: " + dept.getDepartmentName());
        String sql = "UPDATE departments SET department_name=?, manager_id=? WHERE department_id=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getDepartmentName());
            ps.setInt(2, managerId);
            ps.setInt(3, dept.getDepartmentId());
            boolean result = ps.executeUpdate() > 0;
            log.info("Update department result: " + result);
            return result;
        } catch (Exception e) {
            log.error("Error updating department: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete department
    public boolean deleteDepartment(int departmentId) {
        log.info("Deleting department ID: " + departmentId);
        String sql = "DELETE FROM departments WHERE department_id=?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            boolean result = ps.executeUpdate() > 0;
            log.info("Delete department result: " + result);
            return result;
        } catch (Exception e) {
            log.error("Error deleting department: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all departments (with manager name)
    public List<Department> getAllDepartments() {
        log.debug("Fetching all departments");
        List<Department> list = new ArrayList<>();
        String sql = "SELECT d.*, e.first_name, e.last_name FROM departments d LEFT JOIN employees e ON d.manager_id = e.employee_id";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String managerName = rs.getString("first_name") != null ? rs.getString("first_name") + " " + rs.getString("last_name") : null;
                Department dept = new Department(
                        rs.getInt("department_id"),
                        rs.getString("department_name"),
                        managerName
                );
                list.add(dept);
            }
            log.info("Total departments fetched: " + list.size());
        } catch (Exception e) {
            log.error("Error fetching all departments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    // Search departments by name or manager name
    public List<Department> searchDepartments(String keyword) {
        log.debug("Searching departments with keyword: " + keyword);
        List<Department> list = new ArrayList<>();
        String sql = "SELECT d.*, e.first_name, e.last_name FROM departments d LEFT JOIN employees e ON d.manager_id = e.employee_id WHERE d.department_name LIKE ? OR CONCAT(e.first_name, ' ', e.last_name) LIKE ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String managerName = rs.getString("first_name") != null ? rs.getString("first_name") + " " + rs.getString("last_name") : null;
                    Department dept = new Department(
                            rs.getInt("department_id"),
                            rs.getString("department_name"),
                            managerName
                    );
                    list.add(dept);
                }
                log.info("Total search results: " + list.size());
            }
        } catch (Exception e) {
            log.error("Error searching departments: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}