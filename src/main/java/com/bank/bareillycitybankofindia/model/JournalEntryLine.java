package com.bank.bareillycitybankofindia.model;

import java.math.BigDecimal;

public class JournalEntryLine {
    private long id;
    private long entryId;
    private int ledgerAccountId;
    private BigDecimal debitAmount = BigDecimal.ZERO;
    private BigDecimal creditAmount = BigDecimal.ZERO;

    public JournalEntryLine() {}

    public JournalEntryLine(int ledgerAccountId, BigDecimal debitAmount, BigDecimal creditAmount) {
        this.ledgerAccountId = ledgerAccountId;
        this.debitAmount = debitAmount != null ? debitAmount : BigDecimal.ZERO;
        this.creditAmount = creditAmount != null ? creditAmount : BigDecimal.ZERO;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getEntryId() { return entryId; }
    public void setEntryId(long entryId) { this.entryId = entryId; }

    public int getLedgerAccountId() { return ledgerAccountId; }
    public void setLedgerAccountId(int ledgerAccountId) { this.ledgerAccountId = ledgerAccountId; }

    public BigDecimal getDebitAmount() { return debitAmount; }
    public void setDebitAmount(BigDecimal debitAmount) { this.debitAmount = debitAmount; }

    public BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(BigDecimal creditAmount) { this.creditAmount = creditAmount; }
}
