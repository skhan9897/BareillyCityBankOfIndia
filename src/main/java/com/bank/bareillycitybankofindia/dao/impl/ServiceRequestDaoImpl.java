package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.ServiceRequestDao;
import com.bank.bareillycitybankofindia.model.ServiceRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDaoImpl implements ServiceRequestDao {

    @Override
    public long createRequest(ServiceRequest request) {
        String sql = "INSERT INTO service_requests (request_ref, customer_id, request_type, details, status) VALUES (?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, request.getRequestRef());
            ps.setLong(2, request.getCustomerId());
            ps.setString(3, request.getRequestType());
            ps.setString(4, request.getDetails());
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
    public List<ServiceRequest> findByCustomerId(long customerId) {
        List<ServiceRequest> list = new ArrayList<>();
        String sql = "SELECT sr.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.customer_number " +
                     "FROM service_requests sr JOIN customers c ON sr.customer_id = c.id WHERE sr.customer_id = ? ORDER BY sr.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRequest(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ServiceRequest> findAll() {
        List<ServiceRequest> list = new ArrayList<>();
        String sql = "SELECT sr.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.customer_number " +
                     "FROM service_requests sr JOIN customers c ON sr.customer_id = c.id ORDER BY sr.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRequest(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ServiceRequest findById(long id) {
        String sql = "SELECT sr.*, CONCAT(c.first_name, ' ', c.last_name) AS customer_name, c.customer_number " +
                     "FROM service_requests sr JOIN customers c ON sr.customer_id = c.id WHERE sr.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRequest(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateStatus(long id, String status, Long assignedTo, String notes) {
        String sql = "UPDATE service_requests SET status = ?, assigned_to = ?, resolution_notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            if (assignedTo != null) ps.setLong(2, assignedTo); else ps.setNull(2, Types.BIGINT);
            ps.setString(3, notes);
            ps.setLong(4, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServiceRequest mapRequest(ResultSet rs) throws SQLException {
        ServiceRequest sr = new ServiceRequest();
        sr.setId(rs.getLong("id"));
        sr.setRequestRef(rs.getString("request_ref"));
        sr.setCustomerId(rs.getLong("customer_id"));
        sr.setRequestType(rs.getString("request_type"));
        sr.setDetails(rs.getString("details"));
        sr.setStatus(rs.getString("status"));
        long assigned = rs.getLong("assigned_to");
        if (!rs.wasNull()) sr.setAssignedTo(assigned);
        sr.setResolutionNotes(rs.getString("resolution_notes"));
        sr.setCreatedAt(rs.getTimestamp("created_at"));
        sr.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            sr.setCustomerName(rs.getString("customer_name"));
            sr.setCustomerNumber(rs.getString("customer_number"));
        } catch (Exception ignored) {}
        return sr;
    }
}
