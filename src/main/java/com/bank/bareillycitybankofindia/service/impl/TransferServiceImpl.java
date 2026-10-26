package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.accounting.AccountingEngine;
import com.bank.bareillycitybankofindia.dao.TransactionDao;
import com.bank.bareillycitybankofindia.dao.impl.TransactionDaoImpl;
import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.service.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class TransferServiceImpl implements TransferService {

    private final TransactionDao transactionDao = new TransactionDaoImpl();

    @Override
    public Transfer executeTransfer(long fromAccountId, long toAccountId, BigDecimal amount, String description, long initiatedBy, String idempotencyKey) throws Exception {
        return AccountingEngine.processTransfer(fromAccountId, toAccountId, amount, description, initiatedBy, idempotencyKey);
    }

    @Override
    public Transfer getTransactionByRef(String ref) {
        return transactionDao.findByRef(ref);
    }

    @Override
    public List<Transfer> getAccountStatement(long accountId, int limit) {
        return transactionDao.findByAccountId(accountId, limit);
    }

    @Override
    public List<Transfer> getCustomerTransactions(long customerId, int limit) {
        return transactionDao.findByCustomerId(customerId, limit);
    }
}
