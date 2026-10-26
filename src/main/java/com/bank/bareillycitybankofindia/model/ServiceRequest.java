package com.bank.bareillycitybankofindia.model;

import java.sql.Timestamp;

public class ServiceRequest {
    private long id;
    private String requestRef;
    private long customerId;
    private String requestType; // CHEQUE_BOOK, DEBIT_CARD, ADDRESS_UPDATE, DORMANT_REACTIVATION, ACCOUNT_CLOSURE, INTEREST_CERTIFICATE
    private String details;
    private String status; // PENDING, IN_PROGRESS, RESOLVED, REJECTED
    private Long assignedTo;
    private String resolutionNotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Transient
    private String customerName;
    private String customerNumber;

    public ServiceRequest() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRequestRef() { return requestRef; }
    public void setRequestRef(String requestRef) { this.requestRef = requestRef; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }

    public String getResolutionNotes() { return resolutionNotes; }
    public void setResolutionNotes(String resolutionNotes) { this.resolutionNotes = resolutionNotes; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }
}
