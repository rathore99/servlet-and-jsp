package com.example.dao;

import com.example.utils.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeUpdateRequestDAO {
    // private static final Logger log = Logger.getLogger(EmployeeUpdateRequestDAO.class); // log4j 1.x
    private static final Logger log = LogManager.getLogger(EmployeeUpdateRequestDAO.class); // log4j 2.x
    // Add a new update request
    public boolean addRequest(int employeeId, int requestedBy, Map<String, Object> newValues) {
        // Logger logger = Logger.getInstance();
        // logger.info("Adding update request for employee: " + employeeId + ", requested by: " + requestedBy);
        log.info("Adding update request for employee: " + employeeId + ", requested by: " + requestedBy);
        String sql = "INSERT INTO employee_update_requests (employee_id, requested_by, requested_at, status, new_first_name, new_last_name, new_email, new_phone_number, new_hire_date, new_job_id, new_salary, new_department_id) VALUES (?, ?, NOW(), 'PENDING', ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setInt(2, requestedBy);
                ps.setString(3, (String) newValues.get("firstName"));
                ps.setString(4, (String) newValues.get("lastName"));
                ps.setString(5, (String) newValues.get("email"));
                ps.setString(6, (String) newValues.get("phoneNumber"));
                ps.setDate(7, (Date) newValues.get("hireDate"));
                ps.setString(8, (String) newValues.get("jobId"));
                if (newValues.get("salary") != null) {
                    ps.setDouble(9, (Double) newValues.get("salary"));
                } else {
                    ps.setNull(9, Types.DECIMAL);
                }
                if (newValues.get("departmentId") != null) {
                    ps.setInt(10, (Integer) newValues.get("departmentId"));
                } else {
                    ps.setNull(10, Types.INTEGER);
                }
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Add update request result: " + result);
                log.info("Add update request result: " + result);
                return result;
            }
        } catch (Exception e) {
            // logger.error("Error adding update request: " + e.getMessage());
            log.error("Error adding update request: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Approve a request and apply changes
    public boolean approveRequest(int requestId, int approvedBy) {
        // Logger logger = Logger.getInstance();
        // logger.info("Approving update request: " + requestId + ", approved by: " + approvedBy);
        log.info("Approving update request: " + requestId + ", approved by: " + approvedBy);
        // 1. Get the request
        // 2. Update the employee
        // 3. Set request status to APPROVED
        // (Implementation would require transaction management)
        // For brevity, only status update is shown here
        String sql = "UPDATE employee_update_requests SET status='APPROVED', approved_by=?, approved_at=NOW() WHERE request_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, approvedBy);
                ps.setInt(2, requestId);
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Approve request result: " + result);
                log.info("Approve request result: " + result);
                return result;
            }
        } catch (Exception e) {
            // logger.error("Error approving update request: " + e.getMessage());
            log.error("Error approving update request: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Reject a request
    public boolean rejectRequest(int requestId, int approvedBy) {
        // Logger logger = Logger.getInstance();
        // logger.info("Rejecting update request: " + requestId + ", rejected by: " + approvedBy);
        log.info("Rejecting update request: " + requestId + ", rejected by: " + approvedBy);
        String sql = "UPDATE employee_update_requests SET status='REJECTED', approved_by=?, approved_at=NOW() WHERE request_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, approvedBy);
                ps.setInt(2, requestId);
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Reject request result: " + result);
                log.info("Reject request result: " + result);
                return result;
            }
        } catch (Exception e) {
            // logger.error("Error rejecting update request: " + e.getMessage());
            log.error("Error rejecting update request: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Get all pending requests for a manager (by department or direct reports)
    public List<Map<String, Object>> getPendingRequestsForManager(int managerId) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Fetching pending requests for manager: " + managerId);
        log.debug("Fetching pending requests for manager: " + managerId);
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT r.* FROM employee_update_requests r JOIN employees e ON r.employee_id = e.employee_id WHERE r.status='PENDING' AND (e.manager_id=? OR ? IN (SELECT manager_id FROM departments WHERE department_id=e.department_id))";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, managerId);
                ps.setInt(2, managerId);
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= colCount; i++) {
                            row.put(meta.getColumnName(i), rs.getObject(i));
                        }
                        list.add(row);
                    }
                    // logger.info("Total pending requests fetched: " + list.size());
                    log.info("Total pending requests fetched: " + list.size());
                }
            }
        } catch (Exception e) {
            // logger.error("Error fetching pending requests: " + e.getMessage());
            log.error("Error fetching pending requests: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return list;
    }

    // Get a request by ID
    public Map<String, Object> getRequestById(int requestId) {
        // Logger logger = Logger.getInstance();
        // logger.debug("Fetching update request by ID: " + requestId);
        log.debug("Fetching update request by ID: " + requestId);
        String sql = "SELECT * FROM employee_update_requests WHERE request_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, requestId);
                try (ResultSet rs = ps.executeQuery()) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    if (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= colCount; i++) {
                            row.put(meta.getColumnName(i), rs.getObject(i));
                        }
                        // logger.info("Update request found: " + requestId);
                        log.info("Update request found: " + requestId);
                        return row;
                    }
                }
            }
        } catch (Exception e) {
            // logger.error("Error fetching update request by ID: " + e.getMessage());
            log.error("Error fetching update request by ID: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        // logger.warn("No update request found for ID: " + requestId);
        log.warn("No update request found for ID: " + requestId);
        return null;
    }
} 