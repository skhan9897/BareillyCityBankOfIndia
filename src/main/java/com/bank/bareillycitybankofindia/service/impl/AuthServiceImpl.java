package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.dao.CustomerDao;
import com.bank.bareillycitybankofindia.dao.UserDao;
import com.bank.bareillycitybankofindia.dao.impl.AccountDaoImpl;
import com.bank.bareillycitybankofindia.dao.impl.CustomerDaoImpl;
import com.bank.bareillycitybankofindia.dao.impl.UserDaoImpl;
import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.security.BCryptUtil;
import com.bank.bareillycitybankofindia.security.InputValidator;
import com.bank.bareillycitybankofindia.security.PasswordPolicy;
import com.bank.bareillycitybankofindia.service.AuthService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class AuthServiceImpl implements AuthService {

    private final UserDao userDao = new UserDaoImpl();
    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();

    @Override
    public User authenticate(String username, String rawPassword) throws Exception {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        if ("LOCKED".equalsIgnoreCase(user.getStatus())) {
            if (user.getLockoutUntil() != null && user.getLockoutUntil().before(new Timestamp(System.currentTimeMillis()))) {
                userDao.resetFailedLogin(user.getId());
                userDao.updateStatus(user.getId(), "ACTIVE");
            } else {
                throw new IllegalStateException("Account is locked due to multiple failed login attempts. Please try again later.");
            }
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Your account status is " + user.getStatus() + ". Please contact customer support.");
        }

        boolean match = BCryptUtil.verifyPassword(rawPassword, user.getPasswordHash());
        if (!match) {
            userDao.recordFailedLogin(username);
            if (user.getFailedLoginAttempts() + 1 >= 5) {
                userDao.lockUser(user.getId(), 15);
                throw new IllegalStateException("Account locked due to 5 consecutive failed login attempts.");
            }
            throw new IllegalArgumentException("Invalid username or password.");
        }

        userDao.resetFailedLogin(user.getId());
        return user;
    }

    @Override
    public User registerCustomer(String username, String email, String mobile, String rawPassword, String firstName, String lastName, String dob, String gender, String aadhaar, String pan, String address, int branchId, int productId) throws Exception {
        if (!InputValidator.isValidEmail(email)) throw new IllegalArgumentException("Invalid email format.");
        if (!InputValidator.isValidMobile(mobile)) throw new IllegalArgumentException("Invalid mobile number. Enter a valid 10-digit Indian mobile number.");
        if (!PasswordPolicy.isStrongPassword(rawPassword)) throw new IllegalArgumentException(PasswordPolicy.getRequirementsMessage());

        if (userDao.findByUsername(username) != null) throw new IllegalArgumentException("Username already exists.");
        if (userDao.findByEmail(email) != null) throw new IllegalArgumentException("Email already registered.");

        String hash = BCryptUtil.hashPassword(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPasswordHash(hash);
        user.setUserType("CUSTOMER");
        user.setStatus("ACTIVE");

        long userId = userDao.createUser(user, 9); // Role ID 9 = CUSTOMER
        user.setId(userId);

        Customer c = new Customer();
        c.setUserId(userId);
        c.setCustomerNumber("CUST" + System.currentTimeMillis());
        c.setBranchId(branchId > 0 ? branchId : 1);
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setDob(Date.valueOf(dob));
        c.setGender(gender);
        c.setAadhaarMasked(InputValidator.maskAadhaar(aadhaar));
        c.setPanMasked(InputValidator.maskPan(pan));
        c.setAddressLine1(address);
        c.setKycStatus("PENDING");

        long custId = customerDao.createCustomer(c);
        c.setId(custId);

        // Auto-create initial Account with ZERO (0.00) initial balance until funds are deposited!
        Account acc = new Account();
        acc.setAccountNumber("1000" + (100000000 + (long)(Math.random() * 899999999)));
        acc.setCustomerId(custId);
        acc.setBranchId(branchId > 0 ? branchId : 1);
        acc.setProductId(productId > 0 ? productId : 1); // Product 1 = Zero Balance Basic Savings
        acc.setAccountType(productId == 4 ? "CURRENT" : "SAVINGS");
        acc.setAvailableBalance(BigDecimal.ZERO); // Zero initial balance!
        acc.setLedgerBalance(BigDecimal.ZERO);
        acc.setStatus("ACTIVE");

        accountDao.createAccount(acc);

        return user;
    }

    @Override
    public boolean changePassword(long userId, String oldPassword, String newPassword) throws Exception {
        User user = userDao.findById(userId);
        if (user == null) return false;
        if (!BCryptUtil.verifyPassword(oldPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Incorrect current password.");
        }
        if (!PasswordPolicy.isStrongPassword(newPassword)) {
            throw new IllegalArgumentException(PasswordPolicy.getRequirementsMessage());
        }
        String newHash = BCryptUtil.hashPassword(newPassword);
        userDao.updatePassword(userId, newHash);
        return true;
    }

    @Override
    public String generatePasswordResetOtp(String email) throws Exception {
        User user = userDao.findByEmail(email);
        if (user == null) throw new IllegalArgumentException("No account found with this email address.");

        String otp = String.format("%06d", (int)(Math.random() * 1000000));
        String sql = "INSERT INTO otp_challenges (user_id, otp_code, purpose, expires_at) VALUES (?, ?, 'PASSWORD_RESET', ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, otp);
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis() + (10 * 60 * 1000L))); // 10 mins
            ps.executeUpdate();
        }
        return otp;
    }

    @Override
    public boolean resetPasswordWithOtp(String email, String otp, String newPassword) throws Exception {
        User user = userDao.findByEmail(email);
        if (user == null) return false;

        String checkSql = "SELECT id FROM otp_challenges WHERE user_id = ? AND otp_code = ? AND purpose = 'PASSWORD_RESET' AND is_used = false AND expires_at > CURRENT_TIMESTAMP ORDER BY id DESC LIMIT 1";
        long otpId = 0;
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setLong(1, user.getId());
            ps.setString(2, otp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    otpId = rs.getLong(1);
                }
            }
        }

        if (otpId == 0) throw new IllegalArgumentException("Invalid or expired OTP code.");

        if (!PasswordPolicy.isStrongPassword(newPassword)) throw new IllegalArgumentException(PasswordPolicy.getRequirementsMessage());

        String newHash = BCryptUtil.hashPassword(newPassword);
        userDao.updatePassword(user.getId(), newHash);

        String useOtp = "UPDATE otp_challenges SET is_used = true WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(useOtp)) {
            ps.setLong(1, otpId);
            ps.executeUpdate();
        }

        return true;
    }
}
