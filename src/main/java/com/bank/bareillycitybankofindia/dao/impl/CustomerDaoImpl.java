package com.bank.bareillycitybankofindia.dao.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.CustomerDao;
import com.bank.bareillycitybankofindia.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

    @Override
    public Customer findByUserId(long userId) {
        String sql = "SELECT c.*, u.email, u.mobile, b.branch_name FROM customers c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "JOIN branches b ON c.branch_id = b.id WHERE c.user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCustomer(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer findById(long customerId) {
        String sql = "SELECT c.*, u.email, u.mobile, b.branch_name FROM customers c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "JOIN branches b ON c.branch_id = b.id WHERE c.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCustomer(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Customer findByCustomerNumber(String customerNumber) {
        String sql = "SELECT c.*, u.email, u.mobile, b.branch_name FROM customers c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "JOIN branches b ON c.branch_id = b.id WHERE c.customer_number = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapCustomer(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (user_id, customer_number, branch_id, first_name, last_name, dob, gender, aadhaar_masked, pan_masked, address_line1, address_line2, city, state, pincode, kyc_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, customer.getUserId());
            ps.setString(2, customer.getCustomerNumber());
            ps.setInt(3, customer.getBranchId() > 0 ? customer.getBranchId() : 1);
            ps.setString(4, customer.getFirstName());
            ps.setString(5, customer.getLastName());
            ps.setDate(6, customer.getDob());
            ps.setString(7, customer.getGender());
            ps.setString(8, customer.getAadhaarMasked());
            ps.setString(9, customer.getPanMasked());
            ps.setString(10, customer.getAddressLine1());
            ps.setString(11, customer.getAddressLine2());
            ps.setString(12, customer.getCity() != null ? customer.getCity() : "Bareilly");
            ps.setString(13, customer.getState() != null ? customer.getState() : "Uttar Pradesh");
            ps.setString(14, customer.getPincode());
            ps.setString(15, customer.getKycStatus() != null ? customer.getKycStatus() : "PENDING");
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
    public void updateProfile(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, address_line1 = ?, address_line2 = ?, city = ?, state = ?, pincode = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getAddressLine1());
            ps.setString(4, customer.getAddressLine2());
            ps.setString(5, customer.getCity());
            ps.setString(6, customer.getState());
            ps.setString(7, customer.getPincode());
            ps.setLong(8, customer.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateKycStatus(long customerId, String status) {
        String sql = "UPDATE customers SET kyc_status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, customerId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll(int offset, int limit) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT c.*, u.email, u.mobile, b.branch_name FROM customers c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "JOIN branches b ON c.branch_id = b.id ORDER BY c.id DESC LIMIT ? OFFSET ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapCustomer(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Customer> searchCustomers(String query) {
        List<Customer> list = new ArrayList<>();
        String pattern = "%" + query + "%";
        String sql = "SELECT c.*, u.email, u.mobile, b.branch_name FROM customers c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "JOIN branches b ON c.branch_id = b.id " +
                     "WHERE c.customer_number LIKE ? OR c.first_name LIKE ? OR c.last_name LIKE ? OR u.email LIKE ? OR u.mobile LIKE ? LIMIT 20";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            ps.setString(5, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapCustomer(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countCustomers() {
        String sql = "SELECT COUNT(*) FROM customers";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Customer mapCustomer(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setId(rs.getLong("id"));
        c.setUserId(rs.getLong("user_id"));
        c.setCustomerNumber(rs.getString("customer_number"));
        c.setBranchId(rs.getInt("branch_id"));
        c.setFirstName(rs.getString("first_name"));
        c.setLastName(rs.getString("last_name"));
        c.setDob(rs.getDate("dob"));
        c.setGender(rs.getString("gender"));
        c.setAadhaarMasked(rs.getString("aadhaar_masked"));
        c.setPanMasked(rs.getString("pan_masked"));
        c.setAddressLine1(rs.getString("address_line1"));
        c.setAddressLine2(rs.getString("address_line2"));
        c.setCity(rs.getString("city"));
        c.setState(rs.getString("state"));
        c.setPincode(rs.getString("pincode"));
        c.setKycStatus(rs.getString("kyc_status"));
        c.setProfilePicture(rs.getString("profile_picture"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));

        try {
            c.setEmail(rs.getString("email"));
            c.setMobile(rs.getString("mobile"));
            c.setBranchName(rs.getString("branch_name"));
        } catch (Exception ignored) {}

        return c;
    }
}
