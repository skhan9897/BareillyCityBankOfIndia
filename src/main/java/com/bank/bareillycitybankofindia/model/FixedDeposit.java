package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class FixedDeposit {
    private long id;
    private String fdNumber;
    private long accountId;
    private BigDecimal principalAmount;
    private int tenureMonths;
    private BigDecimal interestRate;
    private BigDecimal maturityAmount;
    private Date startDate;
    private Date maturityDate;
    private String status; // ACTIVE, MATURED, PREMATURE_CLOSED
    private BigDecimal prematurePenaltyRate;
    private Timestamp createdAt;

    // Transient
    private String accountNumber;
    private String customerName;

    public FixedDeposit() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getFdNumber() { return fdNumber; }
    public void setFdNumber(String fdNumber) { this.fdNumber = fdNumber; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getPrematurePenaltyRate() { return prematurePenaltyRate; }
    public void setPrematurePenaltyRate(BigDecimal prematurePenaltyRate) { this.prematurePenaltyRate = prematurePenaltyRate; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
