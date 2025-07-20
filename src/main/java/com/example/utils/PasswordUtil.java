package com.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {
    // Use BCrypt if available, otherwise fallback to SHA-256
    public static String hashPassword(String password) {
        try {
            // Try to use BCrypt
            Class.forName("org.mindrot.jbcrypt.BCrypt");
            return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
        } catch (ClassNotFoundException e) {
            // Fallback to SHA-256
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] salt = new byte[16];
                new SecureRandom().nextBytes(salt);
                md.update(salt);
                byte[] hash = md.digest(password.getBytes());
                return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("No hashing algorithm available", ex);
            }
        }
    }

    public static boolean checkPassword(String password, String stored) {
        try {
            // Try to use BCrypt
            Class.forName("org.mindrot.jbcrypt.BCrypt");
            return org.mindrot.jbcrypt.BCrypt.checkpw(password, stored);
        } catch (ClassNotFoundException e) {
            // Fallback to SHA-256
            String[] parts = stored.split(":");
            if (parts.length != 2) return false;
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salt);
                byte[] testHash = md.digest(password.getBytes());
                return MessageDigest.isEqual(hash, testHash);
            } catch (NoSuchAlgorithmException ex) {
                return false;
            }
        }
    }
} 