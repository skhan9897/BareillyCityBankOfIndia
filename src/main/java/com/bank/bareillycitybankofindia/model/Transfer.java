package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transfer {
    private long id;
    private String transactionRef;
    private Long fromAccountId;
    private Long toAccountId;
    private Long journalEntryId;
    private String transactionType; // TRANSFER, DEPOSIT, WITHDRAWAL, BILL_PAYMENT, REVERSAL
    private BigDecimal amount;
    private BigDecimal charge;
    private String status; // SUCCESS, PENDING, FAILED, REJECTED, REVERSED
    private String description;
    private String failureReason;
    private long initiatedBy;
    private String idempotencyKey;
    private Timestamp createdAt;

    // Transient Display Attributes
    private String fromAccountNumber;
    private String toAccountNumber;
    private String initiatorName;

    public Transfer() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTransactionRef() { return transactionRef; }
    public void setTransactionRef(String transactionRef) { this.transactionRef = transactionRef; }

    public Long getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(Long fromAccountId) { this.fromAccountId = fromAccountId; }

    public Long getToAccountId() { return toAccountId; }
    public void setToAccountId(Long toAccountId) { this.toAccountId = toAccountId; }

    public Long getJournalEntryId() { return journalEntryId; }
    public void setJournalEntryId(Long journalEntryId) { this.journalEntryId = journalEntryId; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getCharge() { return charge; }
    public void setCharge(BigDecimal charge) { this.charge = charge; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public long getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(long initiatedBy) { this.initiatedBy = initiatedBy; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getFromAccountNumber() { return fromAccountNumber; }
    public void setFromAccountNumber(String fromAccountNumber) { this.fromAccountNumber = fromAccountNumber; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }

    public String getInitiatorName() { return initiatorName; }
    public void setInitiatorName(String initiatorName) { this.initiatorName = initiatorName; }
}
