package com.example.dao;

import com.example.utils.ConnectionPool;
import com.example.utils.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {
    private static final AppLogger log = AppLogger.getInstance();

    public String getPasswordHashByUsername(String username) {
        String sql = "SELECT password_hash FROM login WHERE username = ?";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }
        } catch (Exception e) {
            log.error("Error fetching password hash for username: " + username);
            e.printStackTrace();
        }
        return null;
    }

    public boolean addLogin(String username, String passwordHash, int employeeId) {
        String sql = "INSERT INTO login (username, password_hash, employee_id) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setInt(3, employeeId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            log.error("Error adding login for username: " + username);
            e.printStackTrace();
            return false;
        }
    }
} 