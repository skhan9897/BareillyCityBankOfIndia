package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class RecurringDeposit {
    private long id;
    private String rdNumber;
    private long accountId;
    private BigDecimal monthlyInstallment;
    private int tenureMonths;
    private BigDecimal interestRate;
    private BigDecimal maturityAmount;
    private int installmentsPaid;
    private Date startDate;
    private Date maturityDate;
    private String status;
    private Timestamp createdAt;

    // Transient
    private String accountNumber;
    private String customerName;

    public RecurringDeposit() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRdNumber() { return rdNumber; }
    public void setRdNumber(String rdNumber) { this.rdNumber = rdNumber; }

    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }

    public BigDecimal getMonthlyInstallment() { return monthlyInstallment; }
    public void setMonthlyInstallment(BigDecimal monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMaturityAmount() { return maturityAmount; }
    public void setMaturityAmount(BigDecimal maturityAmount) { this.maturityAmount = maturityAmount; }

    public int getInstallmentsPaid() { return installmentsPaid; }
    public void setInstallmentsPaid(int installmentsPaid) { this.installmentsPaid = installmentsPaid; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
