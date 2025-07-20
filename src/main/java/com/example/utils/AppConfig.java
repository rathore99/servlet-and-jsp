package com.example.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private Properties properties;

    private AppConfig() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            AppLogger.getInstance().info("Loaded config.properties");
        } catch (IOException e) {
            AppLogger.getInstance().error("Failed to load config.properties: " + e.getMessage());
           // throw new RuntimeException("Failed to load config.properties", e);
            properties.put("db.url","jdbc:mysql://localhost:3307/hr");
            properties.put("db.username","root");
            properties.put("db.password","root");
        }
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}