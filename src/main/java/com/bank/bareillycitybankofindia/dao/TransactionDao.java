package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Transfer;

import java.util.List;

public interface TransactionDao {
    Transfer findById(long id);
    Transfer findByRef(String ref);
    List<Transfer> findByAccountId(long accountId, int limit);
    List<Transfer> findByCustomerId(long customerId, int limit);
    List<Transfer> findAll(int offset, int limit);
    int countTransactions();
}
