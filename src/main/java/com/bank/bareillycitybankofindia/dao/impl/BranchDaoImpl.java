package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.BranchDao;
import com.bank.bareillycitybankofindia.model.Branch;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDaoImpl implements BranchDao {

    @Override
    public List<Branch> findAll() {
        List<Branch> list = new ArrayList<>();
        String sql = "SELECT * FROM branches ORDER BY id ASC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapBranch(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Branch findById(int id) {
        String sql = "SELECT * FROM branches WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapBranch(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int createBranch(Branch b) {
        String sql = "INSERT INTO branches (branch_code, branch_name, ifsc_code, address, city, state, pincode, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, b.getBranchCode());
            ps.setString(2, b.getBranchName());
            ps.setString(3, b.getIfscCode());
            ps.setString(4, b.getAddress());
            ps.setString(5, b.getCity() != null ? b.getCity() : "Bareilly");
            ps.setString(6, b.getState() != null ? b.getState() : "Uttar Pradesh");
            ps.setString(7, b.getPincode());
            ps.setString(8, b.getPhone());
            ps.setString(9, b.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Branch mapBranch(ResultSet rs) throws SQLException {
        Branch b = new Branch();
        b.setId(rs.getInt("id"));
        b.setBranchCode(rs.getString("branch_code"));
        b.setBranchName(rs.getString("branch_name"));
        b.setIfscCode(rs.getString("ifsc_code"));
        b.setAddress(rs.getString("address"));
        b.setCity(rs.getString("city"));
        b.setState(rs.getString("state"));
        b.setPincode(rs.getString("pincode"));
        b.setPhone(rs.getString("phone"));
        b.setEmail(rs.getString("email"));
        b.setStatus(rs.getString("status"));
        b.setCreatedAt(rs.getTimestamp("created_at"));
        return b;
    }
}
