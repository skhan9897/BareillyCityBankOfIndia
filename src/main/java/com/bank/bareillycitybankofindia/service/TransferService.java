package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferService {
    Transfer executeTransfer(long fromAccountId, long toAccountId, BigDecimal amount, String description, long initiatedBy, String idempotencyKey) throws Exception;
    Transfer getTransactionByRef(String ref);
    List<Transfer> getAccountStatement(long accountId, int limit);
    List<Transfer> getCustomerTransactions(long customerId, int limit);
}
