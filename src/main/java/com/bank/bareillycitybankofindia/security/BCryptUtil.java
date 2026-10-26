package com.bank.bareillycitybankofindia.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptUtil {
    private static final int COST = 10;

    public static String hashPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) return null;
        return BCrypt.withDefaults().hashToString(COST, rawPassword.toCharArray());
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) return false;
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
