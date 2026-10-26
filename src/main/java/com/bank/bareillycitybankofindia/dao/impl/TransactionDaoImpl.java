package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.TransactionDao;
import com.bank.bareillycitybankofindia.model.Transfer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public Transfer findById(long id) {
        String sql = "SELECT t.*, fa.account_number AS from_acc_num, ta.account_number AS to_acc_num, u.username AS initiator " +
                     "FROM transactions t " +
                     "LEFT JOIN accounts fa ON t.from_account_id = fa.id " +
                     "LEFT JOIN accounts ta ON t.to_account_id = ta.id " +
                     "LEFT JOIN users u ON t.initiated_by = u.id WHERE t.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapTransaction(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Transfer findByRef(String ref) {
        String sql = "SELECT t.*, fa.account_number AS from_acc_num, ta.account_number AS to_acc_num, u.username AS initiator " +
                     "FROM transactions t " +
                     "LEFT JOIN accounts fa ON t.from_account_id = fa.id " +
                     "LEFT JOIN accounts ta ON t.to_account_id = ta.id " +
                     "LEFT JOIN users u ON t.initiated_by = u.id WHERE t.transaction_ref = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ref);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapTransaction(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transfer> findByAccountId(long accountId, int limit) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, fa.account_number AS from_acc_num, ta.account_number AS to_acc_num, u.username AS initiator " +
                     "FROM transactions t " +
                     "LEFT JOIN accounts fa ON t.from_account_id = fa.id " +
                     "LEFT JOIN accounts ta ON t.to_account_id = ta.id " +
                     "LEFT JOIN users u ON t.initiated_by = u.id " +
                     "WHERE t.from_account_id = ? OR t.to_account_id = ? ORDER BY t.created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            ps.setLong(2, accountId);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transfer> findByCustomerId(long customerId, int limit) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, fa.account_number AS from_acc_num, ta.account_number AS to_acc_num, u.username AS initiator " +
                     "FROM transactions t " +
                     "LEFT JOIN accounts fa ON t.from_account_id = fa.id " +
                     "LEFT JOIN accounts ta ON t.to_account_id = ta.id " +
                     "LEFT JOIN users u ON t.initiated_by = u.id " +
                     "WHERE fa.customer_id = ? OR ta.customer_id = ? ORDER BY t.created_at DESC LIMIT ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.setLong(2, customerId);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Transfer> findAll(int offset, int limit) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, fa.account_number AS from_acc_num, ta.account_number AS to_acc_num, u.username AS initiator " +
                     "FROM transactions t " +
                     "LEFT JOIN accounts fa ON t.from_account_id = fa.id " +
                     "LEFT JOIN accounts ta ON t.to_account_id = ta.id " +
                     "LEFT JOIN users u ON t.initiated_by = u.id ORDER BY t.created_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapTransaction(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countTransactions() {
        String sql = "SELECT COUNT(*) FROM transactions";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Transfer mapTransaction(ResultSet rs) throws SQLException {
        Transfer t = new Transfer();
        t.setId(rs.getLong("id"));
        t.setTransactionRef(rs.getString("transaction_ref"));
        long fromId = rs.getLong("from_account_id");
        if (!rs.wasNull()) t.setFromAccountId(fromId);
        long toId = rs.getLong("to_account_id");
        if (!rs.wasNull()) t.setToAccountId(toId);
        long jeId = rs.getLong("journal_entry_id");
        if (!rs.wasNull()) t.setJournalEntryId(jeId);
        t.setTransactionType(rs.getString("transaction_type"));
        t.setAmount(rs.getBigDecimal("amount"));
        t.setCharge(rs.getBigDecimal("charge"));
        t.setStatus(rs.getString("status"));
        t.setDescription(rs.getString("description"));
        t.setFailureReason(rs.getString("failure_reason"));
        t.setInitiatedBy(rs.getLong("initiated_by"));
        t.setIdempotencyKey(rs.getString("idempotency_key"));
        t.setCreatedAt(rs.getTimestamp("created_at"));

        try {
            t.setFromAccountNumber(rs.getString("from_acc_num"));
            t.setToAccountNumber(rs.getString("to_acc_num"));
            t.setInitiatorName(rs.getString("initiator"));
        } catch (Exception ignored) {}

        return t;
    }
}
