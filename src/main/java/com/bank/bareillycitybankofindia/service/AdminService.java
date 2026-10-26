package com.bank.bareillycitybankofindia.service;

import com.bank.bareillycitybankofindia.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AdminService {
    Map<String, Object> getDashboardStats();
    List<Customer> getAllCustomers(int offset, int limit);
    List<Customer> searchCustomers(String query);
    List<Account> getAllAccounts(int offset, int limit);
    List<Transfer> getAllTransactions(int offset, int limit);
    List<LoanApplication> getAllLoanApplications();
    List<ServiceRequest> getAllServiceRequests();
    List<AuditLog> getAuditLogs(int offset, int limit);
    void approveKyc(long customerId, long reviewerId, String remarks) throws Exception;
    void rejectKyc(long customerId, long reviewerId, String remarks) throws Exception;
    void depositCash(String accountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception;
    void withdrawCash(String accountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception;
    void adminTransfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String description, long employeeUserId) throws Exception;
    void toggleAccountStatus(long accountId, String newStatus, String reason, long employeeUserId) throws Exception;
}
