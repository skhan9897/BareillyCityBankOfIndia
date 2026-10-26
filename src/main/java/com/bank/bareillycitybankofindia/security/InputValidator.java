package com.bank.bareillycitybankofindia.security;

import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern ACCOUNT_NUM_PATTERN = Pattern.compile("^\\d{10,20}$");
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUM_PATTERN.matcher(accountNumber.trim()).matches();
    }

    public static boolean isValidIfsc(String ifsc) {
        return ifsc != null && IFSC_PATTERN.matcher(ifsc.trim().toUpperCase()).matches();
    }

    public static String sanitizeHtml(String input) {
        if (input == null) return null;
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    public static String maskAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.length() < 12) return "XXXX-XXXX-XXXX";
        return "XXXX-XXXX-" + aadhaar.substring(aadhaar.length() - 4);
    }

    public static String maskPan(String pan) {
        if (pan == null || pan.length() < 10) return "XXXXX0000X";
        return pan.substring(0, 2) + "XXXX" + pan.substring(6);
    }

    public static String maskCardNumber(String cardNo) {
        if (cardNo == null || cardNo.length() < 16) return "XXXX-XXXX-XXXX-1234";
        String clean = cardNo.replaceAll("\\D", "");
        return clean.substring(0, 4) + "-XXXX-XXXX-" + clean.substring(clean.length() - 4);
    }
}
