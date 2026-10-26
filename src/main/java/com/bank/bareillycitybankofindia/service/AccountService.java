package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.Account;

import java.util.List;

public interface AccountService {
    Account getAccountById(long id);
    Account getAccountByNumber(String accountNumber);
    List<Account> getAccountsByCustomerId(long customerId);
    Account createAccount(long customerId, int productId, String accountType) throws Exception;
    void freezeAccount(long accountId, String reason) throws Exception;
    void unfreezeAccount(long accountId) throws Exception;
    void requestAccountClosure(long accountId, String reason) throws Exception;
    void changeAccountProduct(long accountId, int newProductId, String reason, long employeeUserId) throws Exception;
    void updateAccountStatusWithAuthorization(long accountId, String newStatus, String reason, long employeeUserId) throws Exception;
}
