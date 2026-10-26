package com.bank.bareillycitybankofindia.model;

import java.sql.Timestamp;

public class AuditLog {
    private long id;
    private Long userId;
    private String actionType;
    private String entityName;
    private String entityId;
    private String details;
    private String ipAddress;
    private Timestamp createdAt;

    // Transient
    private String username;

    public AuditLog() {}

    public AuditLog(Long userId, String actionType, String entityName, String entityId, String details, String ipAddress) {
        this.userId = userId;
        this.actionType = actionType;
        this.entityName = entityName;
        this.entityId = entityId;
        this.details = details;
        this.ipAddress = ipAddress;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getEntityName() { return entityName; }
    public void setEntityName(String entityName) { this.entityName = entityName; }

    public String getEntityId() { return entityId; }
    public void setEntityId(String entityId) { this.entityId = entityId; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
