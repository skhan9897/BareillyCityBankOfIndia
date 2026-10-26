package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.AuditDao;
import com.bank.bareillycitybankofindia.model.AuditLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditDaoImpl implements AuditDao {

    @Override
    public void log(AuditLog log) {
        String sql = "INSERT INTO audit_logs (user_id, action_type, entity_name, entity_id, details, ip_address) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (log.getUserId() != null) ps.setLong(1, log.getUserId()); else ps.setNull(1, Types.BIGINT);
            ps.setString(2, log.getActionType());
            ps.setString(3, log.getEntityName());
            ps.setString(4, log.getEntityId());
            ps.setString(5, log.getDetails());
            ps.setString(6, log.getIpAddress());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AuditLog> findAll(int offset, int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT a.*, u.username FROM audit_logs a LEFT JOIN users u ON a.user_id = u.id ORDER BY a.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setId(rs.getLong("id"));
                    long uid = rs.getLong("user_id");
                    if (!rs.wasNull()) log.setUserId(uid);
                    log.setActionType(rs.getString("action_type"));
                    log.setEntityName(rs.getString("entity_name"));
                    log.setEntityId(rs.getString("entity_id"));
                    log.setDetails(rs.getString("details"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    try { log.setUsername(rs.getString("username")); } catch (Exception ignored) {}
                    list.add(log);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
