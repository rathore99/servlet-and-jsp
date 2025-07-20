package com.example.dao;

import com.example.utils.ConnectionPool;
import com.example.utils.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeAuthDAO {
    // private static final Logger log = Logger.getLogger(EmployeeAuthDAO.class); // log4j 1.x
    private static final Logger log = LogManager.getLogger(EmployeeAuthDAO.class); // log4j 2.x
    // Register a new employee (with unique email and hashed password)
    public boolean registerEmployee(int employeeId, String email, String password) {
        // if (checkEmailExists(email)) return false;
        if (checkEmailExists(email)) return false;
        String hash = PasswordUtil.hashPassword(password);
        String sql = "INSERT INTO employee_auth (employee_id, email, password_hash) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, employeeId);
                ps.setString(2, email);
                ps.setString(3, hash);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error registering employee: " + e.getMessage(), e);
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Check if email already exists
    public boolean checkEmailExists(String email) {
        String sql = "SELECT 1 FROM employee_auth WHERE email=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            log.error("Error checking email existence: " + e.getMessage(), e);
            return true; // fail safe: treat as exists
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

    // Authenticate by email or employee_id
    public Integer authenticate(String username, String password) {
        // Logger logger = Logger.getInstance();
        // logger.info("Authenticating user: " + username);
        log.info("Authenticating user: " + username);
        String sql = "SELECT employee_id, password_hash FROM employee_auth WHERE email=? OR employee_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try {
                    ps.setInt(2, Integer.parseInt(username));
                } catch (NumberFormatException e) {
                    ps.setInt(2, -1); // will not match
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String hash = rs.getString("password_hash");
                        // logger.debug("Found user, checking password hash for: " + username);
                        log.debug("Found user, checking password hash for: " + username);
                        // Try BCrypt first, then SHA-256 fallback
                        boolean match = false;
                        try {
                            Class.forName("org.mindrot.jbcrypt.BCrypt");
                            match = hash.equals(password);//org.mindrot.jbcrypt.BCrypt.checkpw(password, hash);
                        } catch (ClassNotFoundException e) {
                            // logger.warn("BCrypt not found, falling back to SHA-256");
                            log.warn("BCrypt not found, falling back to SHA-256");
                        } catch (Exception e) {
                            // logger.error("BCrypt error: " + e.getMessage());
                            log.error("BCrypt error: " + e.getMessage());
                        }
                        if (!match) {
                            // Try SHA-256 fallback
                            String[] parts = hash.split(":");
                            if (parts.length == 2) {
                                byte[] salt = java.util.Base64.getDecoder().decode(parts[0]);
                                byte[] storedHash = java.util.Base64.getDecoder().decode(parts[1]);
                                try {
                                    java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                                    md.update(salt);
                                    byte[] testHash = md.digest(password.getBytes());
                                    match = java.security.MessageDigest.isEqual(storedHash, testHash);
                                } catch (java.security.NoSuchAlgorithmException ex) {
                                    // logger.error("SHA-256 error: " + ex.getMessage());
                                    log.error("SHA-256 error: " + ex.getMessage());
                                }
                            }
                        }
                        if (match) {
                            // logger.info("Authentication successful for user: " + username);
                            log.info("Authentication successful for user: " + username);
                            return rs.getInt("employee_id");
                        } else {
                            // logger.warn("Authentication failed for user: " + username);
                            log.warn("Authentication failed for user: " + username);
                        }
                    } else {
                        // logger.warn("No user found for: " + username);
                        log.warn("No user found for: " + username);
                    }
                }
            }
        } catch (Exception e) {
            // logger.error("Authentication error for user " + username + ": " + e.getMessage());
            log.error("Authentication error for user " + username + ": " + e.getMessage());
            // e.printStackTrace();
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
        return null;
    }

    // Delete employee
    public boolean deleteEmployee(int id) {
        // Logger logger = Logger.getInstance();
        // logger.info("Deleting employee ID: " + id);
        log.info("Deleting employee ID: " + id);
        String sql = "DELETE FROM employee_auth WHERE employee_id=?";
        Connection conn = null;
        try {
            conn = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                boolean result = ps.executeUpdate() > 0;
                // logger.info("Delete employee result: " + result);
                log.info("Delete employee result: " + result);
                return result;
            }
        } catch (Exception e) {
            log.error("Error deleting employee: " + e.getMessage());
            //log.error("Error deleting employee: " + e.getMessage(), e);
            e.printStackTrace();
            return false;
        } finally {
            ConnectionPool.getInstance().releaseConnection(conn);
        }
    }

} 