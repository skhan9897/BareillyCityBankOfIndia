package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.Customer;

import java.util.List;

public interface CustomerDao {
    Customer findByUserId(long userId);
    Customer findById(long customerId);
    Customer findByCustomerNumber(String customerNumber);
    long createCustomer(Customer customer);
    void updateProfile(Customer customer);
    void updateKycStatus(long customerId, String status);
    List<Customer> findAll(int offset, int limit);
    List<Customer> searchCustomers(String query);
    int countCustomers();
}
