package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static ConnectionPool instance;
    private static final int POOL_SIZE = 10;
    private BlockingQueue<Connection> pool;

    private ConnectionPool() {
        pool = new ArrayBlockingQueue<>(POOL_SIZE);
        AppConfig config = AppConfig.getInstance();
        String url = config.getProperty("db.url");
        String username = config.getProperty("db.username");
        String password = config.getProperty("db.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = DriverManager.getConnection(url, username, password);
                pool.offer(conn);
            }
            AppLogger.getInstance().info("Connection pool initialized with size: " + POOL_SIZE);
        } catch (Exception e) {
            AppLogger.getInstance().error("Failed to initialize connection pool: " + e.getMessage());
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws InterruptedException {
        AppLogger.getInstance().debug("Acquiring connection from pool");
        return pool.take(); // blocks if no connection is available
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            pool.offer(conn);
            AppLogger.getInstance().debug("Connection released back to pool");
        }
    }

    public int availableConnections() {
        return pool.size();
    }
} 