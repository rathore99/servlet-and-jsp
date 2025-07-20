package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static DBUtil instance;
    private static Connection connection;
    private String url;
    private String username;
    private String password;

    private DBUtil() {
        AppConfig config = AppConfig.getInstance();
        url = config.getProperty("db.url");
        username = config.getProperty("db.username");
        password = config.getProperty("db.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            AppLogger.getInstance().info("MySQL JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            AppLogger.getInstance().error("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static DBUtil getInstance() {
        if (instance == null) {
            synchronized (DBUtil.class) {
                if (instance == null) {
                    instance = new DBUtil();
                }
            }
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                AppLogger.getInstance().debug("Opening new DB connection");
                connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            AppLogger.getInstance().error("Failed to get database connection: " + e.getMessage());
            throw new SQLException("Failed to get database connection", e);
        }
        return connection;
    }
}