package com.example.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppLogger {
    private static AppLogger instance;
    private static final Object lock = new Object();
    private PrintWriter writer;
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + File.separator + "application.log";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private AppLogger() {
        try {
            File dir = new File(LOG_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            writer = new PrintWriter(new FileWriter(LOG_FILE, true), true);
        } catch (IOException e) {
            System.err.println("Logger failed to open log file: " + e.getMessage());
        }
    }

    public static AppLogger getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new AppLogger();
                }
            }
        }
        return instance;
    }

    public void log(String level, String message) {
        String logMsg = String.format("[%s] [%s] %s", sdf.format(new Date()), level, message);
        System.out.println(logMsg);
        if (writer != null) {
            writer.println(logMsg);
        }
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
} 