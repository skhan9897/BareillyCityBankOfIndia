package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.DepositDao;
import com.bank.bareillycitybankofindia.model.FixedDeposit;
import com.bank.bareillycitybankofindia.model.RecurringDeposit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepositDaoImpl implements DepositDao {

    @Override
    public long createFixedDeposit(FixedDeposit fd) {
        String sql = "INSERT INTO fixed_deposits (fd_number, account_id, principal_amount, tenure_months, interest_rate, maturity_amount, start_date, maturity_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, fd.getFdNumber());
            ps.setLong(2, fd.getAccountId());
            ps.setBigDecimal(3, fd.getPrincipalAmount());
            ps.setInt(4, fd.getTenureMonths());
            ps.setBigDecimal(5, fd.getInterestRate());
            ps.setBigDecimal(6, fd.getMaturityAmount());
            ps.setDate(7, fd.getStartDate());
            ps.setDate(8, fd.getMaturityDate());
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
    public long createRecurringDeposit(RecurringDeposit rd) {
        String sql = "INSERT INTO recurring_deposits (rd_number, account_id, monthly_installment, tenure_months, interest_rate, maturity_amount, installments_paid, start_date, maturity_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, rd.getRdNumber());
            ps.setLong(2, rd.getAccountId());
            ps.setBigDecimal(3, rd.getMonthlyInstallment());
            ps.setInt(4, rd.getTenureMonths());
            ps.setBigDecimal(5, rd.getInterestRate());
            ps.setBigDecimal(6, rd.getMaturityAmount());
            ps.setInt(7, rd.getInstallmentsPaid());
            ps.setDate(8, rd.getStartDate());
            ps.setDate(9, rd.getMaturityDate());
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
    public List<FixedDeposit> findFdByAccountId(long accountId) {
        List<FixedDeposit> list = new ArrayList<>();
        String sql = "SELECT f.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM fixed_deposits f JOIN accounts a ON f.account_id = a.id JOIN customers c ON a.customer_id = c.id WHERE f.account_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapFd(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<RecurringDeposit> findRdByAccountId(long accountId) {
        List<RecurringDeposit> list = new ArrayList<>();
        String sql = "SELECT r.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM recurring_deposits r JOIN accounts a ON r.account_id = a.id JOIN customers c ON a.customer_id = c.id WHERE r.account_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRd(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public FixedDeposit findFdById(long id) {
        String sql = "SELECT f.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM fixed_deposits f JOIN accounts a ON f.account_id = a.id JOIN customers c ON a.customer_id = c.id WHERE f.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapFd(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RecurringDeposit findRdById(long id) {
        String sql = "SELECT r.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM recurring_deposits r JOIN accounts a ON r.account_id = a.id JOIN customers c ON a.customer_id = c.id WHERE r.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRd(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateFdStatus(long id, String status) {
        String sql = "UPDATE fixed_deposits SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRdStatus(long id, String status) {
        String sql = "UPDATE recurring_deposits SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FixedDeposit> findAllFds() {
        List<FixedDeposit> list = new ArrayList<>();
        String sql = "SELECT f.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM fixed_deposits f JOIN accounts a ON f.account_id = a.id JOIN customers c ON a.customer_id = c.id ORDER BY f.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapFd(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<RecurringDeposit> findAllRds() {
        List<RecurringDeposit> list = new ArrayList<>();
        String sql = "SELECT r.*, a.account_number, CONCAT(c.first_name, ' ', c.last_name) AS customer_name " +
                     "FROM recurring_deposits r JOIN accounts a ON r.account_id = a.id JOIN customers c ON a.customer_id = c.id ORDER BY r.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRd(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private FixedDeposit mapFd(ResultSet rs) throws SQLException {
        FixedDeposit fd = new FixedDeposit();
        fd.setId(rs.getLong("id"));
        fd.setFdNumber(rs.getString("fd_number"));
        fd.setAccountId(rs.getLong("account_id"));
        fd.setPrincipalAmount(rs.getBigDecimal("principal_amount"));
        fd.setTenureMonths(rs.getInt("tenure_months"));
        fd.setInterestRate(rs.getBigDecimal("interest_rate"));
        fd.setMaturityAmount(rs.getBigDecimal("maturity_amount"));
        fd.setStartDate(rs.getDate("start_date"));
        fd.setMaturityDate(rs.getDate("maturity_date"));
        fd.setStatus(rs.getString("status"));
        fd.setPrematurePenaltyRate(rs.getBigDecimal("premature_penalty_rate"));
        fd.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            fd.setAccountNumber(rs.getString("account_number"));
            fd.setCustomerName(rs.getString("customer_name"));
        } catch (Exception ignored) {}
        return fd;
    }

    private RecurringDeposit mapRd(ResultSet rs) throws SQLException {
        RecurringDeposit rd = new RecurringDeposit();
        rd.setId(rs.getLong("id"));
        rd.setRdNumber(rs.getString("rd_number"));
        rd.setAccountId(rs.getLong("account_id"));
        rd.setMonthlyInstallment(rs.getBigDecimal("monthly_installment"));
        rd.setTenureMonths(rs.getInt("tenure_months"));
        rd.setInterestRate(rs.getBigDecimal("interest_rate"));
        rd.setMaturityAmount(rs.getBigDecimal("maturity_amount"));
        rd.setInstallmentsPaid(rs.getInt("installments_paid"));
        rd.setStartDate(rs.getDate("start_date"));
        rd.setMaturityDate(rs.getDate("maturity_date"));
        rd.setStatus(rs.getString("status"));
        rd.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            rd.setAccountNumber(rs.getString("account_number"));
            rd.setCustomerName(rs.getString("customer_name"));
        } catch (Exception ignored) {}
        return rd;
    }
}
