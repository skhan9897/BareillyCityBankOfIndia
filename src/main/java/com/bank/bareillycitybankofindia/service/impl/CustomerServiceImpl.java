package com.bank.bareillycitybankofindia.service.impl;

import com.bank.bareillycitybankofindia.config.DatabaseConfig;
import com.bank.bareillycitybankofindia.dao.CustomerDao;
import com.bank.bareillycitybankofindia.dao.impl.CustomerDaoImpl;
import com.bank.bareillycitybankofindia.model.Customer;
import com.bank.bareillycitybankofindia.model.KycRecord;
import com.bank.bareillycitybankofindia.service.CustomerService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao = new CustomerDaoImpl();

    @Override
    public Customer getCustomerByUserId(long userId) {
        return customerDao.findByUserId(userId);
    }

    @Override
    public Customer getCustomerById(long customerId) {
        return customerDao.findById(customerId);
    }

    @Override
    public void updateProfile(Customer customer) throws Exception {
        customerDao.updateProfile(customer);
    }

    @Override
    public void submitKyc(long customerId, String docType, String docNumberMasked) throws Exception {
        String sql = "INSERT INTO kyc_records (customer_id, document_type, document_number_masked, verification_status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            ps.setString(2, docType);
            ps.setString(3, docNumberMasked);
            ps.executeUpdate();
        }
        customerDao.updateKycStatus(customerId, "PENDING");
    }

    @Override
    public KycRecord getKycRecord(long customerId) {
        String sql = "SELECT * FROM kyc_records WHERE customer_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KycRecord k = new KycRecord();
                    k.setId(rs.getLong("id"));
                    k.setCustomerId(rs.getLong("customer_id"));
                    k.setDocumentType(rs.getString("document_type"));
                    k.setDocumentNumberMasked(rs.getString("document_number_masked"));
                    k.setVerificationStatus(rs.getString("verification_status"));
                    k.setRemarks(rs.getString("remarks"));
                    k.setCreatedAt(rs.getTimestamp("created_at"));
                    return k;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
