package com.bank.bareillycitybankofindia;

import com.bank.bareillycitybankofindia.config.DatabaseInitializer;
import com.bank.bareillycitybankofindia.model.User;
import com.bank.bareillycitybankofindia.security.BCryptUtil;
import com.bank.bareillycitybankofindia.security.PasswordPolicy;
import com.bank.bareillycitybankofindia.service.AuthService;
import com.bank.bareillycitybankofindia.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private static AuthService authService;

    @BeforeAll
    public static void setUp() {
        DatabaseInitializer.initialize();
        authService = new AuthServiceImpl();
    }

    @Test
    @DisplayName("Test BCrypt Password Hashing and Verification")
    public void testPasswordHashing() {
        String raw = "BankPassword@123";
        String hashed = BCryptUtil.hashPassword(raw);
        assertNotNull(hashed);
        assertTrue(BCryptUtil.verifyPassword(raw, hashed));
        assertFalse(BCryptUtil.verifyPassword("WrongPassword", hashed));
    }

    @Test
    @DisplayName("Test Strong Password Policy Validation")
    public void testPasswordPolicy() {
        assertTrue(PasswordPolicy.isStrongPassword("Admin@1234"));
        assertFalse(PasswordPolicy.isStrongPassword("weak"));
        assertFalse(PasswordPolicy.isStrongPassword("12345678"));
        assertFalse(PasswordPolicy.isStrongPassword("NoSpecial123"));
    }

    @Test
    @DisplayName("Test User Authentication with Demo Admin Credentials")
    public void testAdminLogin() throws Exception {
        User admin = authService.authenticate("admin", "Admin@123");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertEquals("EMPLOYEE", admin.getUserType());
    }

    @Test
    @DisplayName("Test User Authentication with Demo Customer Credentials")
    public void testCustomerLogin() throws Exception {
        User customer = authService.authenticate("customer1", "Customer@123");
        assertNotNull(customer);
        assertEquals("customer1", customer.getUsername());
        assertEquals("CUSTOMER", customer.getUserType());
    }

    @Test
    @DisplayName("Test Invalid Credentials Rejection")
    public void testInvalidLogin() {
        assertThrows(IllegalArgumentException.class, () -> {
            authService.authenticate("customer1", "InvalidPassword@999");
        });
    }
}
