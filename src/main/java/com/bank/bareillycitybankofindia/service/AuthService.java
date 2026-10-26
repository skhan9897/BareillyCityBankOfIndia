package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.User;

public interface AuthService {
    User authenticate(String username, String rawPassword) throws Exception;
    User registerCustomer(String username, String email, String mobile, String rawPassword, String firstName, String lastName, String dob, String gender, String aadhaar, String pan, String address, int branchId, int productId) throws Exception;
    boolean changePassword(long userId, String oldPassword, String newPassword) throws Exception;
    String generatePasswordResetOtp(String email) throws Exception;
    boolean resetPasswordWithOtp(String email, String otp, String newPassword) throws Exception;
}
