package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.ServiceRequest;

import java.util.List;

public interface ServiceRequestDao {
    long createRequest(ServiceRequest request);
    List<ServiceRequest> findByCustomerId(long customerId);
    List<ServiceRequest> findAll();
    ServiceRequest findById(long id);
    void updateStatus(long id, String status, Long assignedTo, String notes);
}
