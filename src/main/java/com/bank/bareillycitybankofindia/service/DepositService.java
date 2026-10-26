package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.FixedDeposit;
import com.bank.bareillycitybankofindia.model.RecurringDeposit;

import java.math.BigDecimal;
import java.util.List;

public interface DepositService {
    FixedDeposit calculateFd(BigDecimal principal, int tenureMonths, BigDecimal interestRate);
    FixedDeposit createFixedDeposit(long accountId, BigDecimal principal, int tenureMonths, long userId) throws Exception;
    RecurringDeposit createRecurringDeposit(long accountId, BigDecimal monthlyInstallment, int tenureMonths, long userId) throws Exception;
    List<FixedDeposit> getFdsByAccountId(long accountId);
    List<RecurringDeposit> getRdsByAccountId(long accountId);
    void prematureCloseFd(long fdId, long userId) throws Exception;
}
