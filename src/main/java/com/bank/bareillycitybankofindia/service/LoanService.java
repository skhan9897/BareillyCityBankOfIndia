package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Loan;
import com.bank.bareillycitybankofindia.model.LoanApplication;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualInterestRate, int tenureMonths);
    LoanApplication applyForLoan(long customerId, int productId, BigDecimal requestedAmount, int tenureMonths, String purpose, BigDecimal monthlyIncome) throws Exception;
    List<LoanApplication> getApplicationsByCustomer(long customerId);
    List<Loan> getLoansByCustomer(long customerId);
    void approveAndDisburseLoan(long applicationId, long reviewerId) throws Exception;
    void repayEmi(long loanId, BigDecimal amount) throws Exception;
}
