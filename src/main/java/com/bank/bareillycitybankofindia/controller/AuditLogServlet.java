package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.AuditLog;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/audit-logs")
public class AuditLogServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<AuditLog> logs = adminService.getAuditLogs(0, 100);
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/WEB-INF/views/admin/audit-logs.jsp").forward(req, resp);
    }
}
