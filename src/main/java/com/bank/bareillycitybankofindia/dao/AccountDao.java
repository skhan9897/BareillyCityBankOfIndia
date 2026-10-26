package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    Account findById(long id);
    Account findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(long customerId);
    long createAccount(Account account);
    void updateStatus(long accountId, String status, String reason);
    void updateBalances(long accountId, BigDecimal availableBalance, BigDecimal ledgerBalance);
    List<Account> findAll(int offset, int limit);
    int countAccounts();
    BigDecimal getTotalBankDeposits();
}
