package com.bank.bareillycitybankofindia;

import com.bank.bareillycitybankofindia.config.DatabaseInitializer;
import com.bank.bareillycitybankofindia.model.FixedDeposit;
import com.bank.bareillycitybankofindia.service.DepositService;
import com.bank.bareillycitybankofindia.service.LoanService;
import com.bank.bareillycitybankofindia.service.impl.DepositServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoanAndDepositTest {

    private static LoanService loanService;
    private static DepositService depositService;

    @BeforeAll
    public static void setUp() {
        DatabaseInitializer.initialize();
        loanService = new LoanServiceImpl();
        depositService = new DepositServiceImpl();
    }

    @Test
    @DisplayName("Test EMI Calculation Formula Accuracy")
    public void testEmiCalculation() {
        BigDecimal principal = BigDecimal.valueOf(100000.00); // 1 Lakh
        BigDecimal annualInterestRate = BigDecimal.valueOf(10.0); // 10%
        int tenureMonths = 12; // 1 Year

        BigDecimal emi = loanService.calculateEmi(principal, annualInterestRate, tenureMonths);

        assertNotNull(emi);
        // Formula gives ~8791.59
        assertEquals(BigDecimal.valueOf(8791.59), emi);
    }

    @Test
    @DisplayName("Test Fixed Deposit Interest Calculation")
    public void testFdCalculation() {
        BigDecimal principal = BigDecimal.valueOf(50000.00);
        int tenureMonths = 12;
        BigDecimal interestRate = BigDecimal.valueOf(6.50);

        FixedDeposit fd = depositService.calculateFd(principal, tenureMonths, interestRate);

        assertNotNull(fd);
        assertNotNull(fd.getMaturityAmount());
        assertEquals(1, fd.getMaturityAmount().compareTo(principal));
    }
}
