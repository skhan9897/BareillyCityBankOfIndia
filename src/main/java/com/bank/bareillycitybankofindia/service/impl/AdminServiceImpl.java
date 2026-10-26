package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.accounting.AccountingEngine;
import com.bank.bareillycitybankofindia.dao.*;
import com.bank.bareillycitybankofindia.dao.impl.*;
import com.bank.bareillycitybankofindia.model.*;
import com.bank.bareillycitybankofindia.service.AdminService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminServiceImpl implements AdminService {

    private final CustomerDao customerDao = new CustomerDaoImpl();
    private final AccountDao accountDao = new AccountDaoImpl();
    private final TransactionDao transactionDao = new TransactionDaoImpl();
    private final LoanDao loanDao = new LoanDaoImpl();
    private final ServiceRequestDao serviceRequestDao = new ServiceRequestDaoImpl();
    private final AuditDao auditDao = new AuditDaoImpl();

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerDao.countCustomers());
        stats.put("totalAccounts", accountDao.countAccounts());
        stats.put("totalTransactions", transactionDao.countTransactions());
        stats.put("totalDeposits", accountDao.getTotalBankDeposits());
        stats.put("pendingLoans", loanDao.findAllApplications().stream().filter(a -> "PENDING".equalsIgnoreCase(a.getStatus())).count());
        stats.put("pendingRequests", serviceRequestDao.findAll().stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count());
        return stats;
    }

    @Override
    public List<Customer> getAllCustomers(int offset, int limit) {
        return customerDao.findAll(offset, limit);
    }

    @Override
    public List<Customer> searchCustomers(String query) {
        return customerDao.searchCustomers(query);
    }

    @Override
    public List<Account> getAllAccounts(int offset, int limit) {
        return accountDao.findAll(offset, limit);
    }

    @Override
    public List<Transfer> getAllTransactions(int offset, int limit) {
        return transactionDao.findAll(offset, limit);
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        return loanDao.findAllApplications();
    }

    @Override
    public List<ServiceRequest> getAllServiceRequests() {
        return serviceRequestDao.findAll();
    }

    @Override
    public List<AuditLog> getAuditLogs(int offset, int limit) {
        return auditDao.findAll(offset, limit);
    }

    @Override
    public void approveKyc(long customerId, long reviewerId, String remarks) throws Exception {
        customerDao.updateKycStatus(customerId, "APPROVED");
        auditDao.log(new AuditLog(reviewerId, "KYC_APPROVED", "Customer", String.valueOf(customerId), "KYC Approved: " + remarks, "127.0.0.1"));
    }

    @Override
    public void rejectKyc(long customerId, long reviewerId, String remarks) throws Exception {
        customerDao.updateKycStatus(customerId, "REJECTED");
        auditDao.log(new AuditLog(reviewerId, "KYC_REJECTED", "Customer", String.valueOf(customerId), "KYC Rejected: " + remarks, "127.0.0.1"));
    }

    @Override
    public void depositCash(String accountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception {
        Account account = accountDao.findByAccountNumber(accountNumber);
        if (account == null) throw new IllegalArgumentException("Target customer account not found.");
        AccountingEngine.processDeposit(account.getId(), amount, description != null ? description : "Admin Cash Deposit", employeeUserId);
        auditDao.log(new AuditLog(employeeUserId, "CASH_DEPOSIT", "Account", String.valueOf(account.getId()), "Deposited ₹ " + amount + " into " + accountNumber, "127.0.0.1"));
    }

    @Override
    public void withdrawCash(String accountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception {
        Account account = accountDao.findByAccountNumber(accountNumber);
        if (account == null) throw new IllegalArgumentException("Target customer account not found.");
        AccountingEngine.processWithdrawal(account.getId(), amount, description != null ? description : "Admin Cash Withdrawal", employeeUserId);
        auditDao.log(new AuditLog(employeeUserId, "CASH_WITHDRAWAL", "Account", String.valueOf(account.getId()), "Withdrew ₹ " + amount + " from " + accountNumber, "127.0.0.1"));
    }

    @Override
    public void adminTransfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception {
        Account fromAcc = accountDao.findByAccountNumber(fromAccountNumber);
        Account toAcc = accountDao.findByAccountNumber(toAccountNumber);
        if (fromAcc == null) throw new IllegalArgumentException("Source account not found.");
        if (toAcc == null) throw new IllegalArgumentException("Destination account not found.");
        AccountingEngine.processTransfer(fromAcc.getId(), toAcc.getId(), amount, description != null ? description : "Admin Transfer", employeeUserId, "ADMIN-" + System.currentTimeMillis());
        auditDao.log(new AuditLog(employeeUserId, "ADMIN_TRANSFER", "Account", String.valueOf(fromAcc.getId()), "Transferred ₹ " + amount + " to " + toAccountNumber, "127.0.0.1"));
    }

    @Override
    public void toggleAccountStatus(long accountId, String newStatus, String reason, long employeeUserId) throws Exception {
        accountDao.updateStatus(accountId, newStatus, reason);
        auditDao.log(new AuditLog(employeeUserId, "ACCOUNT_STATUS_CHANGE", "Account", String.valueOf(accountId), "Status changed to " + newStatus + ": " + reason, "127.0.0.1"));
    }
}
