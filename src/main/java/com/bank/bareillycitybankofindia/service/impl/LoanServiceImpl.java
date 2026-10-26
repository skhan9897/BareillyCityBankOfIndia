package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.dao.LoanDao;
import com.bank.bareillycitybankofindia.dao.impl.AccountDaoImpl;
import com.bank.bareillycitybankofindia.dao.impl.LoanDaoImpl;
import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.Loan;
import com.bank.bareillycitybankofindia.model.LoanApplication;
import com.bank.bareillycitybankofindia.service.LoanService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

public class LoanServiceImpl implements LoanService {

    private final LoanDao loanDao = new LoanDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();

    @Override
    public BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualInterestRate, int tenureMonths) {
        // Standard EMI Formula: E = [P x R x (1+R)^N]/[(1+R)^N-1]
        double P = principal.doubleValue();
        double R = annualInterestRate.doubleValue() / 1200.0; // monthly rate
        double N = tenureMonths;

        if (R == 0) return BigDecimal.valueOf(P / N).setScale(2, RoundingMode.HALF_UP);

        double emi = (P * R * Math.pow(1 + R, N)) / (Math.pow(1 + R, N) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public LoanApplication applyForLoan(long customerId, int productId, BigDecimal requestedAmount, int tenureMonths, String purpose, BigDecimal monthlyIncome) throws Exception {
        LoanApplication app = new LoanApplication();
        app.setApplicationRef("LA" + System.currentTimeMillis());
        app.setCustomerId(customerId);
        app.setProductId(productId);
        app.setRequestedAmount(requestedAmount);
        app.setTenureMonths(tenureMonths);
        app.setPurpose(purpose);
        app.setMonthlyIncome(monthlyIncome);
        app.setStatus("PENDING");

        long id = loanDao.createApplication(app);
        app.setId(id);
        return app;
    }

    @Override
    public List<LoanApplication> getApplicationsByCustomer(long customerId) {
        return loanDao.findApplicationsByCustomerId(customerId);
    }

    @Override
    public List<Loan> getLoansByCustomer(long customerId) {
        return loanDao.findLoansByCustomerId(customerId);
    }

    @Override
    public void approveAndDisburseLoan(long applicationId, long reviewerId) throws Exception {
        LoanApplication app = loanDao.findApplicationById(applicationId);
        if (app == null) throw new IllegalArgumentException("Loan application not found.");
        if (!"PENDING".equalsIgnoreCase(app.getStatus()) && !"UNDER_REVIEW".equalsIgnoreCase(app.getStatus())) {
            throw new IllegalStateException("Application is already processed.");
        }

        loanDao.updateApplicationStatus(applicationId, "APPROVED", reviewerId, "Sanctioned and Approved");

        // Disburse loan funds to primary savings account
        List<Account> accounts = accountDao.findByCustomerId(app.getCustomerId());
        if (accounts.isEmpty()) throw new IllegalStateException("Customer does not have an active account for loan disbursement.");
        Account primaryAcc = accounts.get(0);

        BigDecimal rate = BigDecimal.valueOf(9.50); // Default 9.5% p.a.
        BigDecimal emi = calculateEmi(app.getRequestedAmount(), rate, app.getTenureMonths());

        Loan loan = new Loan();
        loan.setLoanAccountNumber("LN" + System.currentTimeMillis());
        loan.setApplicationId(applicationId);
        loan.setCustomerId(app.getCustomerId());
        loan.setAccountId(primaryAcc.getId());
        loan.setSanctionedAmount(app.getRequestedAmount());
        loan.setOutstandingPrincipal(app.getRequestedAmount());
        loan.setInterestRate(rate);
        loan.setTenureMonths(app.getTenureMonths());
        loan.setMonthlyEmi(emi);
        Date startDate = new Date(System.currentTimeMillis());
        loan.setStartDate(startDate);
        loan.setEndDate(Date.valueOf(startDate.toLocalDate().plusMonths(app.getTenureMonths())));
        loan.setStatus("ACTIVE");

        loanDao.createLoan(loan);

        // Credit disbursed funds to account
        BigDecimal newBal = primaryAcc.getAvailableBalance().add(app.getRequestedAmount());
        accountDao.updateBalances(primaryAcc.getId(), newBal, newBal);

        loanDao.updateApplicationStatus(applicationId, "DISBURSED", reviewerId, "Disbursed to " + primaryAcc.getAccountNumber());
    }

    @Override
    public void repayEmi(long loanId, BigDecimal amount) throws Exception {
        Loan loan = loanDao.findLoanById(loanId);
        if (loan == null) throw new IllegalArgumentException("Loan not found.");
        if (!"ACTIVE".equalsIgnoreCase(loan.getStatus())) {
            throw new IllegalStateException("Loan is not active.");
        }

        Account account = accountDao.findById(loan.getAccountId());
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance in linked account to pay EMI.");
        }

        // Deduct from savings
        BigDecimal newAccountBal = account.getAvailableBalance().subtract(amount);
        accountDao.updateBalances(account.getId(), newAccountBal, newAccountBal);

        // Reduce loan outstanding principal
        BigDecimal newOutstanding = loan.getOutstandingPrincipal().subtract(amount);
        if (newOutstanding.compareTo(BigDecimal.ZERO) < 0) newOutstanding = BigDecimal.ZERO;

        loanDao.updateLoanOutstanding(loanId, newOutstanding.doubleValue());
    }
}
