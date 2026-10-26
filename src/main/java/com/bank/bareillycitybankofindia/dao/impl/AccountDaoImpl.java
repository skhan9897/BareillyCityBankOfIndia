package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.AccountDao;
import com.bank.bareillycitybankofindia.model.Account;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

    @Override
    public Account findById(long id) {
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, b.branch_name, b.ifsc_code, p.product_name " +
                     "FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.id " +
                     "JOIN branches b ON a.branch_id = b.id " +
                     "JOIN account_products p ON a.product_id = p.id WHERE a.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapAccount(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, b.branch_name, b.ifsc_code, p.product_name " +
                     "FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.id " +
                     "JOIN branches b ON a.branch_id = b.id " +
                     "JOIN account_products p ON a.product_id = p.id WHERE a.account_number = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapAccount(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> findByCustomerId(long customerId) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, b.branch_name, b.ifsc_code, p.product_name " +
                     "FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.id " +
                     "JOIN branches b ON a.branch_id = b.id " +
                     "JOIN account_products p ON a.product_id = p.id WHERE a.customer_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapAccount(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public long createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, branch_id, product_id, account_type, currency, available_balance, ledger_balance, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getAccountNumber());
            ps.setLong(2, account.getCustomerId());
            ps.setInt(3, account.getBranchId() > 0 ? account.getBranchId() : 1);
            ps.setInt(4, account.getProductId() > 0 ? account.getProductId() : 1);
            ps.setString(5, account.getAccountType());
            ps.setString(6, account.getCurrency() != null ? account.getCurrency() : "INR");
            ps.setBigDecimal(7, account.getAvailableBalance() != null ? account.getAvailableBalance() : BigDecimal.ZERO);
            ps.setBigDecimal(8, account.getLedgerBalance() != null ? account.getLedgerBalance() : BigDecimal.ZERO);
            ps.setString(9, account.getStatus() != null ? account.getStatus() : "ACTIVE");
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void updateStatus(long accountId, String status, String reason) {
        String sql = "UPDATE accounts SET status = ?, closure_reason = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, reason);
            ps.setLong(3, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBalances(long accountId, BigDecimal availableBalance, BigDecimal ledgerBalance) {
        String sql = "UPDATE accounts SET available_balance = ?, ledger_balance = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, availableBalance);
            ps.setBigDecimal(2, ledgerBalance);
            ps.setLong(3, accountId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> findAll(int offset, int limit) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT a.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, b.branch_name, b.ifsc_code, p.product_name " +
                     "FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.id " +
                     "JOIN branches b ON a.branch_id = b.id " +
                     "JOIN account_products p ON a.product_id = p.id ORDER BY a.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapAccount(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countAccounts() {
        String sql = "SELECT COUNT(*) FROM accounts";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public BigDecimal getTotalBankDeposits() {
        String sql = "SELECT SUM(available_balance) FROM accounts WHERE status = 'ACTIVE'";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal sum = rs.getBigDecimal(1);
                return sum != null ? sum : BigDecimal.ZERO;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private Account mapAccount(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setId(rs.getLong("id"));
        a.setAccountNumber(rs.getString("account_number"));
        a.setCustomerId(rs.getLong("customer_id"));
        a.setBranchId(rs.getInt("branch_id"));
        a.setProductId(rs.getInt("product_id"));
        a.setAccountType(rs.getString("account_type"));
        a.setCurrency(rs.getString("currency"));
        a.setAvailableBalance(rs.getBigDecimal("available_balance"));
        a.setLedgerBalance(rs.getBigDecimal("ledger_balance"));
        a.setStatus(rs.getString("status"));
        a.setClosureReason(rs.getString("closure_reason"));
        a.setOpenedAt(rs.getTimestamp("opened_at"));
        a.setUpdatedAt(rs.getTimestamp("updated_at"));

        try {
            a.setCustomerName(rs.getString("customer_name"));
            a.setBranchName(rs.getString("branch_name"));
            a.setIfscCode(rs.getString("ifsc_code"));
            a.setProductName(rs.getString("product_name"));
        } catch (Exception ignored) {}

        return a;
    }
}
