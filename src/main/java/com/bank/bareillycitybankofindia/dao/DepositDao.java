package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.FixedDeposit;
import com.bank.bareillycitybankofindia.model.RecurringDeposit;

import java.util.List;

public interface DepositDao {
    long createFixedDeposit(FixedDeposit fd);
    long createRecurringDeposit(RecurringDeposit rd);
    List<FixedDeposit> findFdByAccountId(long accountId);
    List<RecurringDeposit> findRdByAccountId(long accountId);
    FixedDeposit findFdById(long id);
    RecurringDeposit findRdById(long id);
    void updateFdStatus(long id, String status);
    void updateRdStatus(long id, String status);
    List<FixedDeposit> findAllFds();
    List<RecurringDeposit> findAllRds();
}
