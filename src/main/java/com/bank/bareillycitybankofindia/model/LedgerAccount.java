package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LedgerAccount {
    private int id;
    private String accountCode;
    private String accountName;
    private String category; // ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE
    private BigDecimal balance;
    private Timestamp createdAt;

    public LedgerAccount() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAccountCode() { return accountCode; }
    public void setAccountCode(String accountCode) { this.accountCode = accountCode; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
