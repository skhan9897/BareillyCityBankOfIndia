package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.CardDao;
import com.bank.bareillycitybankofindia.model.Card;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDaoImpl implements CardDao {

    @Override
    public Card findByAccountId(long accountId) {
        String sql = "SELECT c.*, a.account_number, CONCAT(cust.first_name, ' ', cust.last_name) AS customer_name " +
                     "FROM cards c JOIN accounts a ON c.account_id = a.id JOIN customers cust ON a.customer_id = cust.id WHERE c.account_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCard(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card findById(long cardId) {
        String sql = "SELECT c.*, a.account_number, CONCAT(cust.first_name, ' ', cust.last_name) AS customer_name " +
                     "FROM cards c JOIN accounts a ON c.account_id = a.id JOIN customers cust ON a.customer_id = cust.id WHERE c.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCard(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long createCard(Card card) {
        String sql = "INSERT INTO cards (account_id, card_number_masked, card_type, expiry_month, expiry_year, daily_limit, status) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, card.getAccountId());
            ps.setString(2, card.getCardNumberMasked());
            ps.setString(3, card.getCardType() != null ? card.getCardType() : "DEBIT");
            ps.setInt(4, card.getExpiryMonth());
            ps.setInt(5, card.getExpiryYear());
            ps.setBigDecimal(6, card.getDailyLimit() != null ? card.getDailyLimit() : BigDecimal.valueOf(50000));
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
    public void updateStatus(long cardId, String status) {
        String sql = "UPDATE cards SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, cardId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDailyLimit(long cardId, double limit) {
        String sql = "UPDATE cards SET daily_limit = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(limit));
            ps.setLong(2, cardId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> findAll() {
        List<Card> list = new ArrayList<>();
        String sql = "SELECT c.*, a.account_number, CONCAT(cust.first_name, ' ', cust.last_name) AS customer_name " +
                     "FROM cards c JOIN accounts a ON c.account_id = a.id JOIN customers cust ON a.customer_id = cust.id ORDER BY c.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapCard(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Card mapCard(ResultSet rs) throws SQLException {
        Card c = new Card();
        c.setId(rs.getLong("id"));
        c.setAccountId(rs.getLong("account_id"));
        c.setCardNumberMasked(rs.getString("card_number_masked"));
        c.setCardType(rs.getString("card_type"));
        c.setExpiryMonth(rs.getInt("expiry_month"));
        c.setExpiryYear(rs.getInt("expiry_year"));
        c.setDailyLimit(rs.getBigDecimal("daily_limit"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            c.setAccountNumber(rs.getString("account_number"));
            c.setCustomerName(rs.getString("customer_name"));
        } catch (Exception ignored) {}
        return c;
    }
}
