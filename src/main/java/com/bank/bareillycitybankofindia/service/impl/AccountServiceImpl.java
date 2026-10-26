package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.dao.AuditDao;
import com.bank.bareillycitybankofindia.dao.impl.AccountDaoImpl;
import com.bank.bareillycitybankofindia.dao.impl.AuditDaoImpl;
import com.bank.bareillycitybankofindia.model.Account;
import com.bank.bareillycitybankofindia.model.AuditLog;
import com.bank.bareillycitybankofindia.service.AccountService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao = new AccountDaoImpl();

    @Override
    public Account getAccountById(long id) {
        return accountDao.findById(id);
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> getAccountsByCustomerId(long customerId) {
        return accountDao.findByCustomerId(customerId);
    }

    @Override
    public Account createAccount(long customerId, int productId, String accountType) throws Exception {
        Account acc = new Account();
        acc.setAccountNumber("1000" + (100000000 + (long)(Math.random() * 899999999)));
        acc.setCustomerId(customerId);
        acc.setBranchId(1);
        acc.setProductId(productId);
        acc.setAccountType(accountType);
        acc.setAvailableBalance(BigDecimal.ZERO);
        acc.setLedgerBalance(BigDecimal.ZERO);
        acc.setStatus("ACTIVE");

        long id = accountDao.createAccount(acc);
        acc.setId(id);
        return acc;
    }

    @Override
    public void freezeAccount(long accountId, String reason) throws Exception {
        accountDao.updateStatus(accountId, "FROZEN", reason);
    }

    @Override
    public void unfreezeAccount(long accountId) throws Exception {
        accountDao.updateStatus(accountId, "ACTIVE", "Unfrozen by administrator");
    }

    @Override
    public void requestAccountClosure(long accountId, String reason) throws Exception {
        Account acc = accountDao.findById(accountId);
        if (acc == null) throw new IllegalArgumentException("Account not found.");
        if (acc.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Account balance must be 0.00 before requesting closure. Please transfer remaining balance.");
        }
        accountDao.updateStatus(accountId, "CLOSED", reason);
    }

    @Override
    public void changeAccountProduct(long accountId, int newProductId, String reason, long employeeUserId) throws Exception {
        Account acc = accountDao.findById(accountId);
        if (acc == null) throw new IllegalArgumentException("Account not found.");

        String sql = "UPDATE accounts SET product_id = ?, account_type = ? WHERE id = ?";
        String type = (newProductId == 4) ? "CURRENT" : "SAVINGS";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newProductId);
            ps.setString(2, type);
            ps.setLong(3, accountId);
            ps.executeUpdate();
        }

        AuditDao auditDao = new AuditDaoImpl();
        auditDao.log(new AuditLog(employeeUserId, "PRODUCT_UPGRADE", "Account", String.valueOf(accountId), "Upgraded to Product ID: " + newProductId + ". Reason: " + reason, "127.0.0.1"));
    }

    @Override
    public void updateAccountStatusWithAuthorization(long accountId, String newStatus, String reason, long employeeUserId) throws Exception {
        Account acc = accountDao.findById(accountId);
        if (acc == null) throw new IllegalArgumentException("Account not found.");

        if ("CLOSED".equalsIgnoreCase(newStatus) && acc.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Account balance must be 0.00 before closing. Available balance: ₹ " + acc.getAvailableBalance());
        }

        accountDao.updateStatus(accountId, newStatus, reason);

        AuditDao auditDao = new AuditDaoImpl();
        auditDao.log(new AuditLog(employeeUserId, "ACCOUNT_CONTROL_STATUS", "Account", String.valueOf(accountId), "Status updated to " + newStatus + ". Reason: " + reason, "127.0.0.1"));
    }
}
