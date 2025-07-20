package com.example.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashed) {
        if (hashed == null) return false;
        return BCrypt.checkpw(plainPassword, hashed);
    }
} 