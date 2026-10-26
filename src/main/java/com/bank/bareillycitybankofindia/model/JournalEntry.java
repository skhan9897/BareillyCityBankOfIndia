package com.bank.bareillycitybankofindia.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class JournalEntry {
    private long id;
    private String referenceNo;
    private String description;
    private Long postedBy;
    private Timestamp postedAt;
    private List<JournalEntryLine> lines = new ArrayList<>();

    public JournalEntry() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getPostedBy() { return postedBy; }
    public void setPostedBy(Long postedBy) { this.postedBy = postedBy; }

    public Timestamp getPostedAt() { return postedAt; }
    public void setPostedAt(Timestamp postedAt) { this.postedAt = postedAt; }

    public List<JournalEntryLine> getLines() { return lines; }
    public void setLines(List<JournalEntryLine> lines) { this.lines = lines; }

    public void addLine(JournalEntryLine line) {
        lines.add(line);
    }
}
