package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.EmployeeDao;
import com.bank.bareillycitybankofindia.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {

    @Override
    public Employee findByUserId(long userId) {
        String sql = "SELECT e.*, u.username, u.email, b.branch_name, r.name AS role_name " +
                     "FROM employees e " +
                     "JOIN users u ON e.user_id = u.id " +
                     "JOIN branches b ON e.branch_id = b.id " +
                     "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                     "LEFT JOIN roles r ON ur.role_id = r.id WHERE e.user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapEmployee(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT e.*, u.username, u.email, b.branch_name, r.name AS role_name " +
                     "FROM employees e " +
                     "JOIN users u ON e.user_id = u.id " +
                     "JOIN branches b ON e.branch_id = b.id " +
                     "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
                     "LEFT JOIN roles r ON ur.role_id = r.id ORDER BY e.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapEmployee(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public long createEmployee(Employee e) {
        String sql = "INSERT INTO employees (user_id, employee_code, branch_id, first_name, last_name, designation, department) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, e.getUserId());
            ps.setString(2, e.getEmployeeCode());
            ps.setInt(3, e.getBranchId() > 0 ? e.getBranchId() : 1);
            ps.setString(4, e.getFirstName());
            ps.setString(5, e.getLastName());
            ps.setString(6, e.getDesignation());
            ps.setString(7, e.getDepartment());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee e = new Employee();
        e.setId(rs.getLong("id"));
        e.setUserId(rs.getLong("user_id"));
        e.setEmployeeCode(rs.getString("employee_code"));
        e.setBranchId(rs.getInt("branch_id"));
        e.setFirstName(rs.getString("first_name"));
        e.setLastName(rs.getString("last_name"));
        e.setDesignation(rs.getString("designation"));
        e.setDepartment(rs.getString("department"));
        e.setStatus(rs.getString("status"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        try {
            e.setUsername(rs.getString("username"));
            e.setEmail(rs.getString("email"));
            e.setBranchName(rs.getString("branch_name"));
            e.setRoleName(rs.getString("role_name"));
        } catch (Exception ignored) {}
        return e;
    }
}
