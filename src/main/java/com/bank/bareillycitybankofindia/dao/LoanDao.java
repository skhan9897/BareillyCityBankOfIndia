package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Loan;
import com.bank.bareillycitybankofindia.model.LoanApplication;

import java.util.List;

public interface LoanDao {
    long createApplication(LoanApplication app);
    List<LoanApplication> findApplicationsByCustomerId(long customerId);
    List<LoanApplication> findAllApplications();
    LoanApplication findApplicationById(long id);
    void updateApplicationStatus(long id, String status, Long reviewerId, String remarks);

    long createLoan(Loan loan);
    List<Loan> findLoansByCustomerId(long customerId);
    List<Loan> findAllLoans();
    Loan findLoanById(long id);
    void updateLoanOutstanding(long loanId, double newBalance);
}
