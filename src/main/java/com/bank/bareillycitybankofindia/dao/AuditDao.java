package com.bank.bareillycitybankofindia.dao;

import com.bank.bareillycitybankofindia.model.AuditLog;

import java.util.List;

public interface AuditDao {
    void log(AuditLog log);
    List<AuditLog> findAll(int offset, int limit);
}
