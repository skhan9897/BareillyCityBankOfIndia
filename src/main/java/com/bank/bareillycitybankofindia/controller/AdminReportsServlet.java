package com.bank.bareillycitybankofindia.controller;

import com.bank.bareillycitybankofindia.model.Transfer;
import com.bank.bareillycitybankofindia.service.AdminService;
import com.bank.bareillycitybankofindia.service.ReportService;
import com.bank.bareillycitybankofindia.service.impl.AdminServiceImpl;
import com.bank.bareillycitybankofindia.service.impl.ReportServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/reports", "/admin/reports/csv"})
public class AdminReportsServlet extends HttpServlet {

    private final AdminService adminService = new AdminServiceImpl();
    private final ReportService reportService = new ReportServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        List<Transfer> transactions = adminService.getAllTransactions(0, 500);

        if ("/admin/reports/csv".equals(path)) {
            resp.setContentType("text/csv");
            resp.setHeader("Content-Disposition", "attachment; filename=\"Master_Transactions_Report.csv\"");
            try {
                reportService.generateTransactionsCsv(transactions, resp.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        req.setAttribute("transactions", transactions);
        req.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(req, resp);
    }
}
