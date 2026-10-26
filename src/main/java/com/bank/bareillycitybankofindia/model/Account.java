package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {
    private long id;
    private String accountNumber;
    private long customerId;
    private int branchId;
    private int productId;
    private String accountType; // SAVINGS, CURRENT
    private String currency;
    private BigDecimal availableBalance;
    private BigDecimal ledgerBalance;
    private String status; // ACTIVE, FROZEN, DORMANT, CLOSED, RESTRICTED
    private String closureReason;
    private Timestamp openedAt;
    private Timestamp updatedAt;

    // Transient fields
    private String customerName;
    private String branchName;
    private String ifscCode;
    private String productName;

    public Account() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public long getCustomerId() { return customerId; }
    public void setCustomerId(long customerId) { this.customerId = customerId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public BigDecimal getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(BigDecimal availableBalance) { this.availableBalance = availableBalance; }

    public BigDecimal getLedgerBalance() { return ledgerBalance; }
    public void setLedgerBalance(BigDecimal ledgerBalance) { this.ledgerBalance = ledgerBalance; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getClosureReason() { return closureReason; }
    public void setClosureReason(String closureReason) { this.closureReason = closureReason; }

    public Timestamp getOpenedAt() { return openedAt; }
    public void setOpenedAt(Timestamp openedAt) { this.openedAt = openedAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
}
