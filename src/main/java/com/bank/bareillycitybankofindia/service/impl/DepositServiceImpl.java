package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.dao.DepositDao;
import com.bank.bareillycitybankofindia.dao.impl.AccountDaoImpl;
import com.bank.bareillycitybankofindia.dao.impl.DepositDaoImpl;
import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.FixedDeposit;
import com.bank.bareillycitybankofindia.model.RecurringDeposit;
import com.bank.bareillycitybankofindia.service.DepositService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

public class DepositServiceImpl implements DepositService {

    private final DepositDao depositDao = new DepositDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();

    @Override
    public FixedDeposit calculateFd(BigDecimal principal, int tenureMonths, BigDecimal interestRate) {
        // Compound interest quarterly: A = P * (1 + r/400)^(4 * t_years)
        double P = principal.doubleValue();
        double r = interestRate.doubleValue() / 100.0;
        double t = tenureMonths / 12.0;
        double n = 4.0; // quarterly

        double amount = P * Math.pow(1 + (r / n), n * t);

        FixedDeposit fd = new FixedDeposit();
        fd.setPrincipalAmount(principal);
        fd.setTenureMonths(tenureMonths);
        fd.setInterestRate(interestRate);
        fd.setMaturityAmount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP));
        return fd;
    }

    @Override
    public FixedDeposit createFixedDeposit(long accountId, BigDecimal principal, int tenureMonths, long userId) throws Exception {
        Account account = accountDao.findById(accountId);
        if (account == null) throw new IllegalArgumentException("Account not found.");
        if (account.getAvailableBalance().compareTo(principal) < 0) {
            throw new IllegalStateException("Insufficient balance to create Fixed Deposit.");
        }

        BigDecimal rate = BigDecimal.valueOf(6.50); // Default 6.5% p.a.
        if (tenureMonths >= 12 && tenureMonths < 24) rate = BigDecimal.valueOf(7.10);
        else if (tenureMonths >= 24) rate = BigDecimal.valueOf(7.50);

        FixedDeposit calc = calculateFd(principal, tenureMonths, rate);

        String fdNum = "FD" + System.currentTimeMillis();
        calc.setFdNumber(fdNum);
        calc.setAccountId(accountId);
        Date startDate = new Date(System.currentTimeMillis());
        Date maturityDate = Date.valueOf(startDate.toLocalDate().plusMonths(tenureMonths));
        calc.setStartDate(startDate);
        calc.setMaturityDate(maturityDate);
        calc.setStatus("ACTIVE");

        // Deduct from savings balance
        BigDecimal newBal = account.getAvailableBalance().subtract(principal);
        accountDao.updateBalances(accountId, newBal, newBal);

        long id = depositDao.createFixedDeposit(calc);
        calc.setId(id);
        return calc;
    }

    @Override
    public RecurringDeposit createRecurringDeposit(long accountId, BigDecimal monthlyInstallment, int tenureMonths, long userId) throws Exception {
        Account account = accountDao.findById(accountId);
        if (account == null) throw new IllegalArgumentException("Account not found.");
        if (account.getAvailableBalance().compareTo(monthlyInstallment) < 0) {
            throw new IllegalStateException("Insufficient balance for first RD installment.");
        }

        BigDecimal rate = BigDecimal.valueOf(6.80);
        // Formula: M = P * n + P * n*(n+1)/2 * (r/12)
        double P = monthlyInstallment.doubleValue();
        double n = tenureMonths;
        double r = rate.doubleValue() / 100.0;
        double maturity = (P * n) + (P * (n * (n + 1) / 2.0) * (r / 12.0));

        RecurringDeposit rd = new RecurringDeposit();
        rd.setRdNumber("RD" + System.currentTimeMillis());
        rd.setAccountId(accountId);
        rd.setMonthlyInstallment(monthlyInstallment);
        rd.setTenureMonths(tenureMonths);
        rd.setInterestRate(rate);
        rd.setMaturityAmount(BigDecimal.valueOf(maturity).setScale(2, RoundingMode.HALF_UP));
        rd.setInstallmentsPaid(1);
        Date startDate = new Date(System.currentTimeMillis());
        rd.setStartDate(startDate);
        rd.setMaturityDate(Date.valueOf(startDate.toLocalDate().plusMonths(tenureMonths)));
        rd.setStatus("ACTIVE");

        BigDecimal newBal = account.getAvailableBalance().subtract(monthlyInstallment);
        accountDao.updateBalances(accountId, newBal, newBal);

        long id = depositDao.createRecurringDeposit(rd);
        rd.setId(id);
        return rd;
    }

    @Override
    public List<FixedDeposit> getFdsByAccountId(long accountId) {
        return depositDao.findFdByAccountId(accountId);
    }

    @Override
    public List<RecurringDeposit> getRdsByAccountId(long accountId) {
        return depositDao.findRdByAccountId(accountId);
    }

    @Override
    public void prematureCloseFd(long fdId, long userId) throws Exception {
        FixedDeposit fd = depositDao.findFdById(fdId);
        if (fd == null) throw new IllegalArgumentException("Fixed Deposit not found.");
        if (!"ACTIVE".equalsIgnoreCase(fd.getStatus())) {
            throw new IllegalStateException("FD is already closed or matured.");
        }

        // Apply 1% penalty on interest rate
        BigDecimal penaltyRate = fd.getInterestRate().subtract(BigDecimal.ONE);
        if (penaltyRate.compareTo(BigDecimal.ZERO) < 0) penaltyRate = BigDecimal.ZERO;

        // Refund principal + partial interest
        BigDecimal payout = fd.getPrincipalAmount().add(fd.getPrincipalAmount().multiply(penaltyRate).multiply(BigDecimal.valueOf(0.02)));

        Account account = accountDao.findById(fd.getAccountId());
        BigDecimal newBal = account.getAvailableBalance().add(payout);
        accountDao.updateBalances(account.getId(), newBal, newBal);

        depositDao.updateFdStatus(fdId, "PREMATURE_CLOSED");
    }
}
