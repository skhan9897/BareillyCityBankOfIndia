package com.bank.bareillycitybankofindia.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static synchronized void initialize() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            logger.info("Initializing Database Schema and Seed Data...");
            executeSqlScript(conn, "schema.sql");
            executeSqlScript(conn, "seed.sql");
            seedDefaultAccounts(conn);
            logger.info("Database Initialization Complete.");
        } catch (Exception e) {
            logger.error("Failed to initialize database: {}", e.getMessage(), e);
        }
    }

    private static void executeSqlScript(Connection conn, String scriptName) {
        try (InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream(scriptName)) {
            if (is == null) {
                logger.warn("SQL script {} not found in classpath.", scriptName);
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.startsWith("--") || trimmed.startsWith("//")) {
                    continue;
                }
                sb.append(line).append("\n");
            }

            String[] statements = sb.toString().split(";");
            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        try {
                            stmt.execute(trimmed);
                        } catch (Exception ex) {
                            logger.warn("SQL execution note: {}", ex.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error executing SQL script {}: {}", scriptName, e.getMessage());
        }
    }

    private static void seedDefaultAccounts(Connection conn) throws Exception {
        String adminPasswordHash = BCrypt.withDefaults().hashToString(10, "Admin@123".toCharArray());
        String managerPasswordHash = BCrypt.withDefaults().hashToString(10, "Manager@123".toCharArray());
        String officerPasswordHash = BCrypt.withDefaults().hashToString(10, "Officer@123".toCharArray());
        String cashierPasswordHash = BCrypt.withDefaults().hashToString(10, "Cashier@123".toCharArray());
        String customerPasswordHash = BCrypt.withDefaults().hashToString(10, "Customer@123".toCharArray());

        // 1. Super Admin
        long superAdminUserId = ensureUser(conn, "admin", "admin@bareillybank.com", "9876543210", adminPasswordHash, "EMPLOYEE", 1);
        ensureEmployee(conn, superAdminUserId, "EMP001", 1, "System", "Admin", "Super Admin", "IT");

        // 2. Branch Manager
        long managerUserId = ensureUser(conn, "manager", "manager@bareillybank.com", "9876543211", managerPasswordHash, "EMPLOYEE", 3);
        ensureEmployee(conn, managerUserId, "EMP002", 1, "Rajesh", "Sharma", "Branch Manager", "Operations");

        // 3. Loan Officer
        long officerUserId = ensureUser(conn, "loanofficer", "officer@bareillybank.com", "9876543212", officerPasswordHash, "EMPLOYEE", 6);
        ensureEmployee(conn, officerUserId, "EMP003", 1, "Anita", "Verma", "Senior Loan Officer", "Credit");

        // 4. Cashier
        long cashierUserId = ensureUser(conn, "cashier", "cashier@bareillybank.com", "9876543213", cashierPasswordHash, "EMPLOYEE", 5);
        ensureEmployee(conn, cashierUserId, "EMP004", 1, "Vikram", "Singh", "Senior Cashier", "Teller");

        // 5. Customer 1
        long cust1UserId = ensureUser(conn, "customer1", "rahul.kumar@gmail.com", "9876543214", customerPasswordHash, "CUSTOMER", 9);
        long cust1Id = ensureCustomer(conn, cust1UserId, "CUST1001", 1, "Rahul", "Kumar", "1992-05-15", "Male", "XXXX-XXXX-1234", "ABCDE1234F", "APPROVED");

        // Create Accounts for Customer 1
        long savingsAccId = ensureAccount(conn, "100020003001", cust1Id, 1, 1, "SAVINGS", 250000.00, 250000.00);
        ensureAccount(conn, "100020003002", cust1Id, 1, 3, "CURRENT", 500000.00, 500000.00);

        // Ensure Virtual Debit Card for Savings Account
        ensureCard(conn, savingsAccId, "4532-XXXX-XXXX-9821", "DEBIT", 12, 2028, 100000.00);

        // Ensure Virtual UPI Handle
        ensureUpiHandle(conn, savingsAccId, "rahulkumar@bcbi", "upi://pay?pa=rahulkumar@bcbi&pn=Rahul%20Kumar&mc=0000");

        // 6. Customer 2 (for transfers)
        long cust2UserId = ensureUser(conn, "customer2", "priya.singh@gmail.com", "9876543215", customerPasswordHash, "CUSTOMER", 9);
        long cust2Id = ensureCustomer(conn, cust2UserId, "CUST1002", 1, "Priya", "Singh", "1995-08-20", "Female", "XXXX-XXXX-5678", "XYZDE5678G", "APPROVED");
        ensureAccount(conn, "100020003003", cust2Id, 1, 1, "SAVINGS", 120000.00, 120000.00);

        // Ensure Beneficiary
        ensureBeneficiary(conn, cust1Id, "Priya Singh", "100020003003", "BCBI0000001", "ACTIVE");
    }

    private static long ensureUser(Connection conn, String username, String email, String mobile, String passwordHash, String userType, int roleId) throws Exception {
        String select = "SELECT id, password_hash FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long userId = rs.getLong("id");
                    // Update password hash to match fresh seed
                    String updatePw = "UPDATE users SET password_hash = ? WHERE id = ?";
                    try (PreparedStatement psUp = conn.prepareStatement(updatePw)) {
                        psUp.setString(1, passwordHash);
                        psUp.setLong(2, userId);
                        psUp.executeUpdate();
                    }
                    return userId;
                }
            }
        }

        String insert = "INSERT INTO users (username, email, mobile, password_hash, user_type, status, email_verified, mobile_verified) VALUES (?, ?, ?, ?, ?, 'ACTIVE', true, true)";
        try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setString(4, passwordHash);
            ps.setString(5, userType);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long userId = rs.getLong(1);
                    String insertRole = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                    try (PreparedStatement psRole = conn.prepareStatement(insertRole)) {
                        psRole.setLong(1, userId);
                        psRole.setInt(2, roleId);
                        psRole.executeUpdate();
                    }
                    return userId;
                }
            }
        }
        return 0;
    }

    private static long ensureEmployee(Connection conn, long userId, String empCode, int branchId, String firstName, String lastName, String designation, String department) throws Exception {
        String select = "SELECT id FROM employees WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }
        String insert = "INSERT INTO employees (user_id, employee_code, branch_id, first_name, last_name, designation, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, empCode);
            ps.setInt(3, branchId);
            ps.setString(4, firstName);
            ps.setString(5, lastName);
            ps.setString(6, designation);
            ps.setString(7, department);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }

    private static long ensureCustomer(Connection conn, long userId, String custNum, int branchId, String firstName, String lastName, String dob, String gender, String aadhaar, String pan, String kycStatus) throws Exception {
        String select = "SELECT id FROM customers WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }
        String insert = "INSERT INTO customers (user_id, customer_number, branch_id, first_name, last_name, dob, gender, aadhaar_masked, pan_masked, address_line1, city, state, pincode, kyc_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Civil Lines', 'Bareilly', 'Uttar Pradesh', '243001', ?)";
        try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setString(2, custNum);
            ps.setInt(3, branchId);
            ps.setString(4, firstName);
            ps.setString(5, lastName);
            ps.setDate(6, Date.valueOf(dob));
            ps.setString(7, gender);
            ps.setString(8, aadhaar);
            ps.setString(9, pan);
            ps.setString(10, kycStatus);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }

    private static long ensureAccount(Connection conn, String accNum, long customerId, int branchId, int productId, String accType, double balance, double ledgerBalance) throws Exception {
        String select = "SELECT id FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setString(1, accNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("id");
            }
        }
        String insert = "INSERT INTO accounts (account_number, customer_id, branch_id, product_id, account_type, available_balance, ledger_balance, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, accNum);
            ps.setLong(2, customerId);
            ps.setInt(3, branchId);
            ps.setInt(4, productId);
            ps.setString(5, accType);
            ps.setBigDecimal(6, BigDecimal.valueOf(balance));
            ps.setBigDecimal(7, BigDecimal.valueOf(ledgerBalance));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        return 0;
    }

    private static void ensureCard(Connection conn, long accountId, String cardMasked, String type, int expM, int expY, double limit) throws Exception {
        String select = "SELECT id FROM cards WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        String insert = "INSERT INTO cards (account_id, card_number_masked, card_type, expiry_month, expiry_year, daily_limit, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setLong(1, accountId);
            ps.setString(2, cardMasked);
            ps.setString(3, type);
            ps.setInt(4, expM);
            ps.setInt(5, expY);
            ps.setBigDecimal(6, BigDecimal.valueOf(limit));
            ps.executeUpdate();
        }
    }

    private static void ensureUpiHandle(Connection conn, long accountId, String upiId, String qrData) throws Exception {
        String select = "SELECT id FROM virtual_upi_handles WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        String insert = "INSERT INTO virtual_upi_handles (account_id, upi_id, qr_code_data, status) VALUES (?, ?, ?, 'ACTIVE')";
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setLong(1, accountId);
            ps.setString(2, upiId);
            ps.setString(3, qrData);
            ps.executeUpdate();
        }
    }

    private static void ensureBeneficiary(Connection conn, long customerId, String name, String accNum, String ifsc, String status) throws Exception {
        String select = "SELECT id FROM beneficiaries WHERE customer_id = ? AND account_number = ?";
        try (PreparedStatement ps = conn.prepareStatement(select)) {
            ps.setLong(1, customerId);
            ps.setString(2, accNum);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return;
            }
        }
        String insert = "INSERT INTO beneficiaries (customer_id, beneficiary_name, account_number, ifsc_code, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insert)) {
            ps.setLong(1, customerId);
            ps.setString(2, name);
            ps.setString(3, accNum);
            ps.setString(4, ifsc);
            ps.setString(5, status);
            ps.executeUpdate();
        }
    }
}
