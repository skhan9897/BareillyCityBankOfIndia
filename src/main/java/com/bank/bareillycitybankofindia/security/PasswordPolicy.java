package com.bank.bareillycitybankofindia.security;

import java.util.regex.Pattern;

public class PasswordPolicy {
    // Min 8 chars, at least 1 uppercase, 1 lowercase, 1 digit, 1 special char
    private static final Pattern STRENGTH_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_.-]).{8,64}$");

    public static boolean isStrongPassword(String password) {
        if (password == null) return false;
        return STRENGTH_PATTERN.matcher(password).matches();
    }

    public static String getRequirementsMessage() {
        return "Password must be 8-64 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=!_.-).";
    }
}
