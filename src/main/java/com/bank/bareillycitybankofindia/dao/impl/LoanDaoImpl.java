package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.LoanDao;
import com.bank.bareillycitybankofindia.model.Loan;
import com.bank.bareillycitybankofindia.model.LoanApplication;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao {

    @Override
    public long createApplication(LoanApplication app) {
        String sql = "INSERT INTO loan_applications (application_ref, customer_id, product_id, requested_amount, tenure_months, purpose, monthly_income, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, app.getApplicationRef());
            ps.setLong(2, app.getCustomerId());
            ps.setInt(3, app.getProductId());
            ps.setBigDecimal(4, app.getRequestedAmount());
            ps.setInt(5, app.getTenureMonths());
            ps.setString(6, app.getPurpose());
            ps.setBigDecimal(7, app.getMonthlyIncome());
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
    public List<LoanApplication> findApplicationsByCustomerId(long customerId) {
        List<LoanApplication> list = new ArrayList<>();
        String sql = "SELECT la.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, lp.product_name " +
                     "FROM loan_applications la JOIN customers c ON la.customer_id = c.id JOIN loan_products lp ON la.product_id = lp.id WHERE la.customer_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapApplication(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LoanApplication> findAllApplications() {
        List<LoanApplication> list = new ArrayList<>();
        String sql = "SELECT la.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, lp.product_name " +
                     "FROM loan_applications la JOIN customers c ON la.customer_id = c.id JOIN loan_products lp ON la.product_id = lp.id ORDER BY la.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapApplication(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public LoanApplication findApplicationById(long id) {
        String sql = "SELECT la.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, lp.product_name " +
                     "FROM loan_applications la JOIN customers c ON la.customer_id = c.id JOIN loan_products lp ON la.product_id = lp.id WHERE la.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapApplication(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateApplicationStatus(long id, String status, Long reviewerId, String remarks) {
        String sql = "UPDATE loan_applications SET status = ?, reviewer_id = ?, remarks = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            if (reviewerId != null) ps.setLong(2, reviewerId); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, remarks);
            ps.setLong(4, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long createLoan(Loan loan) {
        String sql = "INSERT INTO loans (loan_account_number, application_id, customer_id, account_id, sanctioned_amount, outstanding_principal, interest_rate, tenure_months, monthly_emi, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, loan.getLoanAccountNumber());
            ps.setLong(2, loan.getApplicationId());
            ps.setLong(3, loan.getCustomerId());
            ps.setLong(4, loan.getAccountId());
            ps.setBigDecimal(5, loan.getSanctionedAmount());
            ps.setBigDecimal(6, loan.getOutstandingPrincipal());
            ps.setBigDecimal(7, loan.getInterestRate());
            ps.setInt(8, loan.getTenureMonths());
            ps.setBigDecimal(9, loan.getMonthlyEmi());
            ps.setDate(10, loan.getStartDate());
            ps.setDate(11, loan.getEndDate());
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
    public List<Loan> findLoansByCustomerId(long customerId) {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT l.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, a.account_number " +
                     "FROM loans l JOIN customers c ON l.customer_id = c.id JOIN accounts a ON l.account_id = a.id WHERE l.customer_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapLoan(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Loan> findAllLoans() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT l.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, a.account_number " +
                     "FROM loans l JOIN customers c ON l.customer_id = c.id JOIN accounts a ON l.account_id = a.id ORDER BY l.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapLoan(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Loan findLoanById(long id) {
        String sql = "SELECT l.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, a.account_number " +
                     "FROM loans l JOIN customers c ON l.customer_id = c.id JOIN accounts a ON l.account_id = a.id WHERE l.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapLoan(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateLoanOutstanding(long loanId, double newBalance) {
        String sql = "UPDATE loans SET outstanding_principal = ?, status = CASE WHEN ? <= 0 THEN 'CLOSED' ELSE status END WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(newBalance));
            ps.setBigDecimal(2, BigDecimal.valueOf(newBalance));
            ps.setLong(3, loanId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private LoanApplication mapApplication(ResultSet rs) throws SQLException {
        LoanApplication la = new LoanApplication();
        la.setId(rs.getLong("id"));
        la.setApplicationRef(rs.getString("application_ref"));
        la.setCustomerId(rs.getLong("customer_id"));
        la.setProductId(rs.getInt("product_id"));
        la.setRequestedAmount(rs.getBigDecimal("requested_amount"));
        la.setTenureMonths(rs.getInt("tenure_months"));
        la.setPurpose(rs.getString("purpose"));
        la.setMonthlyIncome(rs.getBigDecimal("monthly_income"));
        la.setStatus(rs.getString("status"));
        long rev = rs.getLong("reviewer_id");
        if (!rs.wasNull()) la.setReviewerId(rev);
        la.setRemarks(rs.getString("remarks"));
        la.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            la.setCustomerName(rs.getString("customer_name"));
            la.setProductName(rs.getString("product_name"));
        } catch (Exception ignored) {}
        return la;
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setId(rs.getLong("id"));
        l.setLoanAccountNumber(rs.getString("loan_account_number"));
        l.setApplicationId(rs.getLong("application_id"));
        l.setCustomerId(rs.getLong("customer_id"));
        l.setAccountId(rs.getLong("account_id"));
        l.setSanctionedAmount(rs.getBigDecimal("sanctioned_amount"));
        l.setOutstandingPrincipal(rs.getBigDecimal("outstanding_principal"));
        l.setInterestRate(rs.getBigDecimal("interest_rate"));
        l.setTenureMonths(rs.getInt("tenure_months"));
        l.setMonthlyEmi(rs.getBigDecimal("monthly_emi"));
        l.setStartDate(rs.getDate("start_date"));
        l.setEndDate(rs.getDate("end_date"));
        l.setStatus(rs.getString("status"));
        l.setDisbursedAt(rs.getTimestamp("disbursed_at"));
        try {
            l.setCustomerName(rs.getString("customer_name"));
            l.setAccountNumber(rs.getString("account_number"));
        } catch (Exception ignored) {}
        return l;
    }
}
