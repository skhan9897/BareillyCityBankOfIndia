package com.bank.bareillycitybankofindia.model;

import java.sql.Timestamp;

public class KycRecord {
    private long id;
    private long customerId;
    private String documentType;
    private String documentNumberMasked;
    private String verificationStatus;
    private Long reviewerId;
    private String remarks;
    private Timestamp reviewedAt;
    private Timestamp createdAt;

    // Transient
    private String customerName;
    private String customerNumber;

    public KycRecord() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getDocumentNumberMasked() { return documentNumberMasked; }
    public void setDocumentNumberMasked(String documentNumberMasked) { this.documentNumberMasked = documentNumberMasked; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Timestamp getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Timestamp reviewedAt) { this.reviewedAt = reviewedAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerNumber() { return customerNumber; }
    public void setCustomerNumber(String customerNumber) { this.customerNumber = customerNumber; }
}
